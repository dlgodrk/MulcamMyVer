package restAPI;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

public class BookAPI {
	public static ArrayList<Book> searchBook(String target, String keyword) throws Exception{
		String key = "f0c2e8a7655a267eac71b54bd7402c2c18f444233955c5bbdb26609f4c4d9154";
    	JSONObject responseJSON;
        StringBuilder urlBuilder = new StringBuilder("https://www.nl.go.kr/NL/search/openApi/search.do"); /*URL*/
        urlBuilder.append("?" + URLEncoder.encode("key","UTF-8") + "="+key); /*인증키*/
        urlBuilder.append("&" + URLEncoder.encode("apiType","UTF-8") + "=json"); /*xml or json*/
        urlBuilder.append("&" + URLEncoder.encode("srchTarget","UTF-8") + "=" + URLEncoder.encode(target,"UTF-8")); /*검색타겟*/
        urlBuilder.append("&" + URLEncoder.encode("kwd","UTF-8") + "=" + URLEncoder.encode(keyword,"UTF-8")); /*검색내용*/
        urlBuilder.append("&" + URLEncoder.encode("pageSize","UTF-8") + "=" + URLEncoder.encode("10", "UTF-8")); /*페이지 크기(기본10)*/
        urlBuilder.append("&" + URLEncoder.encode("pageNum","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*시작 페이지(기본1)*/
        urlBuilder.append("&" + URLEncoder.encode("category","UTF-8") + "=" + URLEncoder.encode("도서", "UTF-8")); /*카테고리*/
        urlBuilder.append("&" + URLEncoder.encode("sort","UTF-8") + "="); /*정렬*/
        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");
        System.out.println("Response code: " + conn.getResponseCode());
        BufferedReader rd;
        if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        rd.close();
        conn.disconnect();
        responseJSON = new JSONObject(sb.toString());
        JSONArray resultArray = responseJSON.getJSONArray("result");
        ArrayList<Book> bookList = new ArrayList();
        for(int i = 0 ; i < resultArray.length(); i++) {
        	JSONObject jo = (JSONObject)resultArray.get(i);
        	bookList.add(JSONToBook(jo));
        }
        return bookList;
	}
	public static Book JSONToBook(JSONObject jo) {
		String str = jo.toString();
		boolean find = false;
		do {
			find = false;
			loop:
			for(int i = 0 ; i < str.length(); i++) {
				char c = str.charAt(i);
				if(c == '<') {
					for(int j = i; j < str.length(); j++) {
						char d = str.charAt(j);
						if(d == '>') {
							str = str.substring(0,i) + str.substring(j+1);
							find = true;
							break loop;
						}
					}
				}
			}
		}while(find);
		jo = new JSONObject(str);
		Book book = new Book();
		String title = jo.getString("titleInfo");
		String author = jo.getString("authorInfo");
		String publisher = jo.getString("pubInfo");
		String pubyear = jo.getString("pubYearInfo");
		String place = jo.getString("placeInfo");
		//int startIndex = title.indexOf('>');
		//int endIndex = title.indexOf('<',2);
		//title = title.substring(startIndex+1,endIndex);
		book.setAuthor(author);
		book.setPlace(place);
		book.setPublisher(publisher);
		book.setPubyear(pubyear);
		book.setTitle(title);
		return book;
	}
}
