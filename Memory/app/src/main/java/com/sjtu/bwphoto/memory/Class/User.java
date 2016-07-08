package com.sjtu.bwphoto.memory.Class;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Timestamp;

/**
 * Created by ly on 7/1/2016.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)  //如果null就不生成
public class User {
    @JsonProperty  //自动转json
    private String name;
    @JsonProperty("password")
    private String pwd;
    @JsonProperty
    private int role;
    @JsonProperty
    private String email;
    @JsonProperty
    private int age;
    @JsonProperty
    private String birth;
    @JsonProperty
    private Timestamp time;

    @JsonCreator  //构造器
    public User(@JsonProperty("name") String name,  //生成json时重命名为name
                @JsonProperty("password") String pwd) {
        this.name = name;
        this.pwd = pwd;
    }

    @JsonCreator  //构造器
    public User(@JsonProperty("name") String name,  //生成json时重命名为name
                @JsonProperty("password") String pwd,
                @JsonProperty("email") String email,
                @JsonProperty("age") int age,
                @JsonProperty("birth") String birth) {
        this.name = name;
        this.pwd = pwd;
        this.email = email;
        this.age = age;
        this.birth = birth;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }


}
