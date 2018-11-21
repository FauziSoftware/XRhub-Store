package online.xrhub.xrhubstore.utils;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;

import java.util.List;

/**
 * @AUTHOR soft
 * @DATE 2018/11/12 22:43
 * @DESCRIBE
 */
@Slf4j
public class XrmUtils {

    @AllArgsConstructor
    public static class XRMJson {
        private JSONObject object;

        public XRMJson addMetaIds(String id) {
            JSONArray meta = (JSONArray) object.get("meta");
            meta.add(id);
            return this;
        }

        public XRMJson addMetaIds(List<String> ids) {
            JSONArray meta = (JSONArray) object.get("meta");
            meta.addAll(ids);
            return this;
        }

        public XRMJson setInfo(int index, String key, String val) {
            JSONObject component = (JSONObject) object.get("component");
            JSONArray  info      = (JSONArray) component.get("info");
            JSONObject io        = (JSONObject) info.get(index);
            io.put(key, val);
            return this;
        }

        @Override
        public String toString() {
            return object.toJSONString();
        }

        public JSONObject to() {
            return object;
        }

        public byte[] toBytes() {
            return toString().getBytes();
        }
    }

    public static XRMJson baseXrm() {
        JSONParser parser = new JSONParser(JSONParser.DEFAULT_PERMISSIVE_MODE);
        JSONObject o = null;
        try {
            o = (JSONObject) parser.parse("{\n" +
                    "    \"meta\": [],\n" +
                    "    \"hierarchy\": [],\n" +
                    "    \"component\": {\n" +
                    "        \"info\": [\n" +
                    "            {\n" +
                    "                \"unit_index\": \"0\",\n" +
                    "                \"name\": \"\",\n" +
                    "                \"description\": \"age\"\n" +
                    "            }\n" +
                    "        ],\n" +
                    "        \"transform\": [\n" +
                    "            {\n" +
                    "                \"unit_index\": 0,\n" +
                    "                \"position\": {\n" +
                    "                    \"x\": 0,\n" +
                    "                    \"y\": 0,\n" +
                    "                    \"z\": 0\n" +
                    "                },\n" +
                    "                \"rotation\": {\n" +
                    "                    \"x\": 0,\n" +
                    "                    \"y\": 0,\n" +
                    "                    \"z\": 0,\n" +
                    "                    \"w\": 1\n" +
                    "                },\n" +
                    "                \"scale\": {\n" +
                    "                    \"x\": 1,\n" +
                    "                    \"y\": 1,\n" +
                    "                    \"z\": 1\n" +
                    "                }\n" +
                    "            }\n" +
                    "        ],\n" +
                    "        \"mesh\": [\n" +
                    "            {\n" +
                    "                \"unit_index\": 0,\n" +
                    "                \"meta_index\": 0\n" +
                    "            }\n" +
                    "        ]\n" +
                    "    }\n" +
                    "}");
        } catch (ParseException ignored) {}
        return new XRMJson(o);
    }
}
