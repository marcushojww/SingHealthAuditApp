package com.c2g4.SingHealthWebApp.Admin.Report;

import java.io.IOException;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

/**
 * Custom Deserialiser for ReportEntry to handle and convert incomplete ReportEntries from the frontend
 * @author LunarFox
 *
 */
public class CustomReportEntryDeserializer extends StdDeserializer<ReportEntry> {
    public CustomReportEntryDeserializer(){
        this(null);
    }

    public CustomReportEntryDeserializer(Class<?> vc){
        super(vc);
    }

    @Override
    public ReportEntry deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) 
    		throws IOException, JsonProcessingException {
        ReportEntry entry = new ReportEntry();
        ObjectCodec codec = jsonParser.getCodec();
        JsonNode node = codec.readTree(jsonParser);

        //internal housekeeping
        entry.setEntry_id(-1); //idk

        entry.setDate(new Date(Calendar.getInstance().getTime().getTime()));
        entry.setTime(new Time(Calendar.getInstance().getTime().getTime()));

        //from json
        JsonNode qn_id_node = node.get("qn_id");
        System.out.println(node.fieldNames());
        entry.setQn_id(qn_id_node.asInt());
        //check pass or fail
        JsonNode passFailNode = node.get("status");
        entry.setStatus(passFailNode.asInt());
        //check if should store remarks and evidence
        JsonNode remarksNode = node.get("remarks");
        if(remarksNode != null) {
            entry.setRemarks(remarksNode.asText());
        }
        if(entry.getStatus() == Component_Status.FAIL){
            int severity = node.get("severity").asInt();
            if(!checkSeverityFormat(severity)) throw new IllegalArgumentException();
            entry.setSeverity(severity);
        }
        JsonNode imageNode = node.get("images");
        if(imageNode != null) {
        	Iterator<JsonNode> it = imageNode.iterator();
        	List<String> strImages = new ArrayList<>(imageNode.size());
        	while(it.hasNext()) {
        		strImages.add(it.next().asText());
        	}
        	entry.setImages(strImages);
        }

        return entry;
    }

    boolean checkSeverityFormat(int severity){
        if(severity<=1000000) return false;
        int DDMMYY = severity%1000000;
        int DDMM = DDMMYY/100;
        int YY = DDMMYY - DDMM*100;
        int DD = DDMM/100;
        int MM = DDMM - DD*100;
        return !(DD == 0 | MM == 0 | YY == 0);
    }
}

