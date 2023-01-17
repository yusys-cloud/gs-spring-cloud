package com.example;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.devtools.restart.classloader.ClassLoaderFiles;

/**
 * @author : yangzq80@gmail.com
 * @date: 2019-05-06
 */
public class ClassFiles {
    ClassLoaderFiles classLoaderFiles;

    void getFiles() {
        ClassLoaderFiles files = new ClassLoaderFiles();

    }
}
