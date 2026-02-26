package com.example.lottery2;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Lottery1Download {
    private final OkHttpClient client = new OkHttpClient();

    private String downloadPageJson(int pageNo) throws IOException {
        String url = "https://www.cwl.gov.cn/cwl_admin/front/cwlkj/search/kjxx/findDrawNotice?" +
                "name=ssq" +
                "&pageSize=30" +
                "&systemType=PC" +
                "&pageNo=" + pageNo;
        Request request = new Request.Builder()
                .url(url)
                .get()
                .header("accept", "application/json, text/javascript, */*; q=0.01")
                //.header("accept-encoding", "gzip, deflate, br, zstd")
                .header("accept-language", "zh-CN,zh;q=0.9")
                //如果下次失败，可通过浏览器进官网（https://www.cwl.gov.cn/ygkj/wqkjgg/ssq/）查看一下某一页，然后查看源码找出当前 cookie 的 HMF_CI 替换
                //如果还是失败，重新查看源码请求 header 并调整调试
                .header("cookie", "HMF_CI=7df81187d8d8b224734b86ff74e6400af65286ddf7bf05ffd25ed669babb6730b0f7c940c24d7c2f0c096ca9117f3d0c5fb78655f91ced4b73e79c0df21056f719; 21_vq=" + pageNo)
                .header("host", "www.cwl.gov.cn")
                .header("priority", "u=1, i")
                .header("referer", "https://www.cwl.gov.cn/ygkj/wqkjgg/")
                .header("sec-ch-ua", "\"Not(A:Brand\";v=\"8\", \"Chromium\";v=\"144\", \"Google Chrome\";v=\"144\"")
                .header("sec-ch-ua-mobile", "?0")
                .header("sec-ch-ua-platform", "\"macOS\"")
                .header("sec-fetch-dest", "empty")
                .header("sec-fetch-mode", "cors")
                .header("sec-fetch-site", "cross-site")
                .header("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/144.0.0.0 Safari/537.36")
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

    private final Gson gson = new Gson();

    private JsonArray parsePageJson(String pageJson) {
        JsonArray pageResult = new JsonArray();

        System.out.println(pageJson);

        JsonArray list = gson.fromJson(pageJson, JsonObject.class)
                .getAsJsonArray("result");

        for (JsonElement jsonElement : list) {
            JsonObject o = (JsonObject) jsonElement;
            String code = o.get("code").getAsString();
            String date = o.get("date").getAsString();
            String red = o.get("red").getAsString();
            String blue = o.get("blue").getAsString();

            JsonObject r = new JsonObject();
            r.addProperty("code", code);
            r.addProperty("date", date);
            r.addProperty("red", red);
            r.addProperty("blue", blue);

            pageResult.add(r);
        }

        return pageResult;
    }

    private void saveResult(JsonArray result) throws IOException {
        try (FileWriter fw = new FileWriter("download_result2.json")) {
            fw.write(result.toString());
            fw.flush();
        }
    }

    private final Random random = new Random();

    public void exec() throws IOException, InterruptedException {
        JsonArray finalResult = new JsonArray();

        for (int pageNo = 1; pageNo <= 66; pageNo++) {
            // 防止 net api 禁止访问
            Thread.sleep(5000 + random.nextInt(10) * 1000);
            String pageJson = downloadPageJson(pageNo);
            //System.out.println(pageNo + ": " + pageJson);
            JsonArray pageResult = parsePageJson(pageJson);
            finalResult.addAll(pageResult);
        }

        saveResult(finalResult);
    }
}
