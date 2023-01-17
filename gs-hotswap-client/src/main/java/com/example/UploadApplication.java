package com.example;

import com.example.hot.Uploader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.devtools.filewatch.ChangedFile;
import org.springframework.boot.devtools.filewatch.ChangedFiles;

import java.io.File;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author yangzq80@gmail.com
 * @date 12/14/22
 */
@Slf4j
public class UploadApplication {

    public static void main(String[] args) {

        String sourceFolder = "/Users/zqy/test/classes";
        String fileName = "com/example/TestThreeController.class";

        log.info("{} --- {}", sourceFolder, fileName);

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
