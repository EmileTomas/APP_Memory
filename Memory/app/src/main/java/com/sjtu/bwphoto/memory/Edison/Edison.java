package com.sjtu.bwphoto.memory.Edison;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Class Edison
 * @since 2016/9/6.
 */
public class Edison
{
	private String ip = "192.168.1.";
	private int tail = 0;
	private int port = 0;
	private Socket soc = null;
	private BufferedReader br = null;
	private BufferedWriter bw = null;

	public Edison(int tail, int port, String auth) throws EdisonError
	{
		// build the tail
		if(!setConAddr(tail))
		{
			// use the broadcast model
			setConAddr(255);
		}

		// try to set a port
		if(port < 0)
		{
			port = 0;
		}
		this.port = port;

		// try to connect to edison
		try
		{
			soc = new Socket("10.189.83.69", this.port);
		}catch (Exception e)
		{
			soc = null;
			throw new EdisonError(e.getMessage());
		}

		// handshaking
		String news = "edison_memory\n";
		try
		{
			br = new BufferedReader(new InputStreamReader(soc.getInputStream()));
			bw = new BufferedWriter(new OutputStreamWriter(soc.getOutputStream()));
			// check connection
			bw.write(news);
			bw.flush();
			news = br.readLine();
			System.out.println("received from edison: " + news);
			if(!"OK".equals(news))
			{
				throw new EdisonError("handshaking failed: "+ news);
			}

			// authentication
			bw.write(auth+'\n');
			bw.flush();
			news = br.readLine();
			System.out.println("received from edison: " + news);
			if(!"OK".equals(news))
			{
				throw new EdisonError("authenticate failed: " + news);
			}
		}catch (Exception e)
		{
			throw new EdisonError(e.getMessage());
		}
		// ready to transfer
	}

	public boolean sendArray(List tars)
	{
		try
		{
			bw.write("object_send\n");
			bw.flush();

			String news = br.readLine();
			System.out.println("sending array: " + news);
			if(!"OK".equals(news))
			{
				throw new EdisonError(news);
			}

			ObjectMapper om = new ObjectMapper();
			String json = om.writeValueAsString(tars);

			bw.write(json+'\n');
			bw.flush();

			news = br.readLine();
			System.out.println("sending array: " + news);
			if(!"OK".equals(news))
			{
				throw new EdisonError(news);
			}

			return true;
		}catch (Exception e)
		{
			System.out.println("Failed when sending array: " + e.getMessage());
			return false;
		} catch (EdisonError edisonError)
		{
			System.out.println("Failed when sending array: " + edisonError.getMessage());
			return false;
		}
	}

	public <T> List<T> getArray(Class<T> clazz)
	{	try
		{
			bw.write("object_get\n");
			bw.flush();

			String news = br.readLine();
			if(!"OK".equals(news))
			{
				throw new EdisonError(news);
			}
			String tar = br.readLine();
			ObjectMapper om = new ObjectMapper();
			JavaType javaType = om.getTypeFactory().constructParametrizedType(List.class, List.class, clazz);
			return (List<T>)om.readValue(tar, javaType);
		}catch (Exception e)
		{
			System.out.println("Failed when getting array: " + e.getMessage());
			return null;
		}catch (EdisonError edisonError)
		{
			System.out.println("Failed when getting array: " + edisonError.getMessage());
			return null;
		}
	}

	public boolean sendFile(String name, String path)
	{
		return false;
	}

	public boolean getFile(String name, String path)
	{
		return false;
	}


	public boolean setConAddr(int tail)
	{
		// out of range
		if(tail < 1 || tail > 254)
		{
			// did not set success
			return false;
		}

		// connect to the mac
		int loc = ip.lastIndexOf('.');
		ip = ip.substring(0, loc + 1);
		ip += tail;
		this.tail = tail;
		return true;
	}

	public void close()
	{
		try
		{
			if(soc != null)
			{
				bw.write("stop\n");
				bw.flush();
				soc.close();
				br.close();
				bw.close();
			}
		}catch (Exception e)
		{}
	}

	public int getTail()
	{
		return this.tail;
	}

	public String getIp()
	{
		return this.ip;
	}

	public int getPort()
	{
		return this.port;
	}

	public static void main(String[] args)
	{
		try{
			Edison edison = new Edison(108, 80, "memory");


			/*
			User user = new User();
			user.setAge(10);
			user.setName("Love");
			List<User> users = new ArrayList<>();
			users.add(user);
			edison.sendArray(users);

*/

			List<EdisonMemory> uss = edison.getArray(EdisonMemory.class);
			for(EdisonMemory tar: uss)
			{

			}
			edison.close();

		}catch (EdisonError e)
		{
			System.out.println(e.getMessage());
		}
	}
}
