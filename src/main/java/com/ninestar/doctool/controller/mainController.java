package com.ninestar.doctool.controller;

import com.ninestar.doctool.config.WebSocketServe;
import com.ninestar.doctool.utils.FileUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RestController
public class mainController {

    @GetMapping("/groupsending")
    public String groupsending(@RequestParam("message") String message){
        WebSocketServe.GroupSending(message);
        return "已群发该消息:"+message;
    }

    @GetMapping("/filesdownloads")
    public ResponseEntity<byte[]> filesdownloads(@RequestParam("path") String path) throws IOException {
        File file = new File(path);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", file.getName());
        return new ResponseEntity<byte[]>(org.apache.commons.io.FileUtils.readFileToByteArray(file),headers, HttpStatus.OK);
    }

    @PostMapping(value = "/import", headers = "content-type=multipart/*")
    public String importSqlLite(@RequestParam("file") MultipartFile file) throws Exception {
        String timename = FileUtils.getTimename();
        String filename = file.getOriginalFilename();
        if(!filename.contains(".zip")){
            return "请上传正确的zip文件";
        }
        String endfilename = FileUtils.downloadPath() + timename + "_" + filename;
        File newFile = new File(endfilename);
        file.transferTo(newFile);
        return FileUtils.uncompress(endfilename);
    }

}
