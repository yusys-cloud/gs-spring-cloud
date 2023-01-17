package com.example.gsmiddleware.hotswap;

import com.example.hot.ClassPathFileChangeListener;
import com.example.hot.Uploader;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.boot.devtools.filewatch.ChangedFile;
import org.springframework.boot.devtools.filewatch.ChangedFiles;
import org.springframework.boot.devtools.filewatch.FileSystemWatcher;
import org.springframework.boot.devtools.restart.classloader.ClassLoaderFile;
import org.springframework.boot.devtools.restart.classloader.ClassLoaderFiles;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.util.Assert;
import org.springframework.util.FileCopyUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.URI;
import java.net.URL;
import java.util.*;
import java.util.regex.Pattern;

/**
 * @author yangzq80@gmail.com
 * @date 12/1/22
 */
@Slf4j
public class DynamicClassLoaderTests {

    @Test
    public void TestInclude() throws Exception {
//        Pattern pattern = new Pattern("/");
    }


    @Test
    public void TestUpload() throws Exception {

//        FileSystemWatcher fileSystemWatcherToStop = new FileSystemWatcher();
//        fileSystemWatcherToStop.addSourceFolder(new File("/Users/zqy/test/classes"));
//
//        fileSystemWatcherToStop.addListener(new ClassPathFileChangeListener());
//
//        fileSystemWatcherToStop.start();

//        String sourceFolder = "/Users/zqy/codes/yusys-cloud/gs-spring-cloud/gs-sc-svc/target/classes";
        String sourceFolder = "/Users/zqy/test/classes";
        String fileName = "com/example/TestController2.class";

        Set<ChangedFiles> changeSet = new LinkedHashSet<>();

        Set<ChangedFile> changes = new LinkedHashSet<>();
        changes.add(new ChangedFile(new File(sourceFolder),
                new File(sourceFolder + "/" + fileName),
                ChangedFile.Type.ADD));

        //TODO 删除与新增验证


        changeSet.add(new ChangedFiles(new File(sourceFolder), changes));


        new Uploader().upload(changeSet);

    }


}