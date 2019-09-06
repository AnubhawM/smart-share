package com.ftd.smartshare.client;


import com.ftd.smartshare.client.commands.SmartShare;
import picocli.CommandLine;
import com.ftd.smartshare.dao.FileDao;
import com.ftd.smartshare.entity.Files;

import java.lang.*;
import java.sql.Timestamp;

class Main {

    public static void main(String[] args) {
    	String str = new String("Anubhaw");
    	byte[] byteArr = str.getBytes();
    	
        FileDao fileDao = new FileDao();

        Files newFile = new Files();
        newFile.setFileName("HopeThisWorks20.txt");
        newFile.setFile(byteArr);        
        newFile.setTimeCreated(new Timestamp(System.currentTimeMillis()));
        newFile.setExpiryTime(new Timestamp(System.currentTimeMillis() + 3600000));
        newFile.setMaxDownloads(10);
        newFile.setTotalDownloads(7);
        newFile.setPassword("1234");
        
        fileDao.createFile(newFile, "1234", 60, 6);

        
        //fileDao.getFileData("HopeThisWorks6.txt");
        //fileDao.deleteEntry("HopeThisWorks5.txt", "1234", 2);
        
        
        
        	
       
    	
    	
//        CommandLine.run(new SmartShare()); // Pass cli arguments here
//        CommandLine.run(new SmartShare(), "-h");
//        CommandLine.run(new SmartShare(), "--version");
//        CommandLine.run(new SmartShare(), "upload");
//        CommandLine.run(new SmartShare(), "upload", "pom.xml");
//        CommandLine.run(new SmartShare(), "upload", ".gitignore");
//        CommandLine.run(new SmartShare(), "upload", "pom.xml", "password");
//        CommandLine.run(new SmartShare(), "download", "pom.xml", "password");
//        CommandLine.run(new SmartShare(), "download", "pom.xml", "wrongpassword");
//        CommandLine.run(new SmartShare(), "download", "test.txt", "password");
//        CommandLine.run(new SmartShare(), "download", "pom.xml", "password");
//        CommandLine.run(new SmartShare(), "view", "pom.xml", "password");
//        CommandLine.run(new SmartShare(), "upload", "pom.xml", "password", "-m", "1");
//        CommandLine.run(new SmartShare(), "download", "pom.xml", "password");
//        CommandLine.run(new SmartShare(), "download", "pom.xml", "password");
        
    }

}

