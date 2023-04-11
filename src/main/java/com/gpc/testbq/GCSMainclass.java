package com.gpc.testbq;

public class GCSMainclass {
    public static void main(String[] args) {
        System.out.println("Hello GCSMainclass!");

        ReadFiles GCSObj = new ReadFiles();
        GCSObj.listObjects("My Project 49701", "testloadbucket2", "SUPPINV/");
        System.out.println("Big Query Load Job completed!");
    }
}
