package com.example.hot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.devtools.filewatch.ChangedFile;
import org.springframework.boot.devtools.filewatch.ChangedFiles;
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
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.URI;
import java.net.URL;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import java.util.Set;

/**
 * @author yangzq80@gmail.com
 * @date 12/13/22
 */

@Slf4j
public class Uploader {

    private static final Map<ChangedFile.Type, ClassLoaderFile.Kind> TYPE_MAPPINGS;

    static {
        Map<ChangedFile.Type, ClassLoaderFile.Kind> map = new EnumMap<>(
                ChangedFile.Type.class);
        map.put(ChangedFile.Type.ADD, ClassLoaderFile.Kind.ADDED);
        map.put(ChangedFile.Type.DELETE, ClassLoaderFile.Kind.DELETED);
        map.put(ChangedFile.Type.MODIFY, ClassLoaderFile.Kind.MODIFIED);
        TYPE_MAPPINGS = Collections.unmodifiableMap(map);
    }


    private ClassLoaderFiles getClassLoaderFiles(Set<ChangedFiles> changedFilesSet)
            throws IOException {
        ClassLoaderFiles files = new ClassLoaderFiles();
        for (ChangedFiles changedFiles : changedFilesSet) {
            String sourceFolder = changedFiles.getSourceFolder().getAbsolutePath();
            for (ChangedFile changedFile : changedFiles) {
                files.addFile(sourceFolder, changedFile.getRelativeName(),
                        asClassLoaderFile(changedFile));
            }
        }
        return files;
    }

    private ClassLoaderFile asClassLoaderFile(ChangedFile changedFile)
            throws IOException {
        ClassLoaderFile.Kind kind = TYPE_MAPPINGS.get(changedFile.getType());
        byte[] bytes = (kind != ClassLoaderFile.Kind.DELETED)
                ? FileCopyUtils.copyToByteArray(changedFile.getFile()) : null;
        long lastModified = (kind != ClassLoaderFile.Kind.DELETED) ? changedFile.getFile().lastModified()
                : System.currentTimeMillis();
        return new ClassLoaderFile(kind, lastModified, bytes);
    }

    private byte[] serialize(ClassLoaderFiles classLoaderFiles) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        objectOutputStream.writeObject(classLoaderFiles);
        objectOutputStream.close();
        return outputStream.toByteArray();
    }


    private void performUpload(ClassLoaderFiles classLoaderFiles, byte[] bytes)
            throws Exception {
        URI uri = new URL("http://localhost:1011/.~~spring-boot!~/restart").toURI();
        try {
            while (true) {
                try {
                    ClientHttpRequest request = new SimpleClientHttpRequestFactory()
                            .createRequest(uri, HttpMethod.POST);
                    HttpHeaders headers = request.getHeaders();
                    headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
                    headers.setContentLength(bytes.length);

                    request.getHeaders().add("X-AUTH-TOKEN", "mysecret");

                    FileCopyUtils.copy(bytes, request.getBody());
                    ClientHttpResponse response = request.execute();
                    HttpStatus statusCode = response.getStatusCode();
                    Assert.state(statusCode == HttpStatus.OK, () -> "Unexpected "
                            + statusCode + " response uploading class files");
                    logUpload(classLoaderFiles);
                    return;
                } catch (ConnectException ex) {
                    log.warn("Failed to connect when uploading to " + uri
                            + ". Upload will be retried in 2 seconds");
                    Thread.sleep(2000);
                }
            }
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException(ex);
        }
    }

    private void logUpload(ClassLoaderFiles classLoaderFiles) {
        int size = classLoaderFiles.size();
        log.info("Uploaded " + size + " class "
                + ((size != 1) ? "resources" : "resource"));
    }

    public void upload(Set<ChangedFiles> changeSet) {

        try {
            ClassLoaderFiles classLoaderFiles = getClassLoaderFiles(changeSet);

            byte[] bytes = serialize(classLoaderFiles);
            performUpload(classLoaderFiles, bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
