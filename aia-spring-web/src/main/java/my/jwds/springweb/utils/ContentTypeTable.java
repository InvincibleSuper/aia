package my.jwds.springweb.utils;

import my.jwds.core.api.InvokeContentType;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContentTypeTable {

    public static Map<InvokeContentType, MediaType[]> contentTypeListMap = new HashMap<>();


    static{
        contentTypeListMap.put(InvokeContentType.file,new MediaType[]{MediaType.MULTIPART_FORM_DATA});
        contentTypeListMap.put(InvokeContentType.json,new MediaType[]{MediaType.APPLICATION_JSON});
        contentTypeListMap.put(InvokeContentType.param,new MediaType[]{MediaType.APPLICATION_FORM_URLENCODED});
    }



    public static MediaType[] find(InvokeContentType contentType){
        return contentTypeListMap.get(contentType);
    }
}
