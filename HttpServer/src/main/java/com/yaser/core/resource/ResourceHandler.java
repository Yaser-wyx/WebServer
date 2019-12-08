package com.yaser.core.resource;

import com.yaser.core.exception.exceptions.ResourceNotFoundException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

//静态资源处理
@Slf4j
public class ResourceHandler {
    @Getter
    private String resourceName;
    public byte[] getResourceByUrl(String url) throws ResourceNotFoundException {
        URL resourceUrl = ResourceHandler.class.getResource(url);
        if (resourceUrl == null) {
            log.error("{}资源未找到", url);
            throw new ResourceNotFoundException();
        }
        byte[] resource = null;
        try {
            File file = new File(resourceUrl.toURI());
            this.resourceName = file.getName();
            FileInputStream fis = new FileInputStream(file);
            resource = new byte[fis.available()];
            fis.read(resource);
            fis.close();
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        }
        return resource;
    }
}
