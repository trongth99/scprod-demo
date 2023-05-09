/**
 * 
 */
package fis.com.vn.ocr;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import org.apache.commons.collections4.map.HashedMap;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import fis.com.vn.api.entities.Params;
import fis.com.vn.common.Utils;
import fis.com.vn.component.ConfigProperties;
import fis.com.vn.repository.TemplateOcrRepository;

/**
 * @author ChinhVD4
 *
 */
@Component
public class ReadContentOcr {
	private static final Logger LOGGER = LoggerFactory.getLogger(ReadContentOcr.class);
	@Autowired TemplateOcrRepository templateOcrRepository;
	@Autowired ConfigProperties configProperties;
	String breakReplace = "xuongDong";
	String breakReplaceSpace = " "+breakReplace+" ";
	String checkLastIndexKeyword = "---";
	String[] listCharTrim = {":", "/", "(", ")", ";", ",", "-", "&"};
	
	public String respJson(ParseTemplaceOcr parseTemplaceOcr, String noiDungOcr) {
		return respJson(parseTemplaceOcr, noiDungOcr, null);
	}
	
	public String respJson(ParseTemplaceOcr parseTemplaceOcr, String noiDungOcr, Params params) {
		JSONObject jsonReturn = new JSONObject();
		try {
			jsonReturn.put("data", fromTemplate(parseTemplaceOcr, noiDungOcr, params));
			jsonReturn.put("status", 200);
			jsonReturn.put("message", "Thành công");
		} catch (Exception e) {
			jsonReturn.put("status", 401);
			jsonReturn.put("message", e.getMessage());
		}
		
		return jsonReturn.toString();
	}
	
	public JSONObject fromTemplate(ParseTemplaceOcr parseTemplaceOcr, String noiDungOcr) throws Exception{
		return fromTemplate(parseTemplaceOcr, noiDungOcr, null);
	}
	
	public JSONObject fromTemplate(ParseTemplaceOcr parseTemplaceOcr, String noiDungOcr, Params params) throws Exception{
		String str = formatContent(noiDungOcr);
		System.out.println(str);
		JSONObject json = new JSONObject();
		
		try {
            Field changeMap = json.getClass().getDeclaredField("map");
            changeMap.setAccessible(true);
            changeMap.set(json, new LinkedHashMap<>());
            changeMap.setAccessible(false);
        } catch (Exception e) {
        }
		
		for (FieldTemplate f : parseTemplaceOcr.getTemplate()) {
			try {
				String strHandling = str;
				
				if(!StringUtils.isEmpty(f.getType()) && !f.getType().equals("table1")) {
					Map<String, String> pageIndex = getPageIndex(str);
					
					Map<String, Integer> mapSaveIndexCuts = new HashedMap<String, Integer>();
					mapSaveIndexCuts.put("start", -1);
					mapSaveIndexCuts.put("end", -1);
					
					strHandling = cutString(strHandling, f, mapSaveIndexCuts);
					
					Map<String, Integer> mapSaveIndex = new HashedMap<String, Integer>();
					mapSaveIndex.put("start", -1);
					mapSaveIndex.put("end", -1);
					
					String value = cutStartEnd(strHandling, f, mapSaveIndex);
					value = split(f, value);
					
					value = replaceAll(value, f);
					
					value = replace(value, f);
					
					String namPage = getPageFromString(pageIndex, mapSaveIndexCuts, mapSaveIndex);
					
					value = replaceAllValue(value, parseTemplaceOcr.getReplace());
					convertTypeValue(value, f, json, params, namPage);
				} else {
					convertTypeValue(strHandling, f, json, params, "");
				}
				
			} catch (Exception e) {
//				LOGGER.error("fromTemplate: {}", e.getMessage());
			}
		}
		return  json;
	}
	/**
	 * @param str
	 * @return
	 */
	public String formatContent(String str) {
		if(!StringUtils.isEmpty(str)) {
			str = str.replace("\\n", breakReplaceSpace).replace("\\r", breakReplaceSpace).replace("\n", breakReplaceSpace).replace("\r", breakReplaceSpace);
			str = str.replaceAll("[ ]+", " ")
					.replaceAll("[\\s]+", " ")
					.trim();
			for (String string : listCharTrim) {
				str = str.replaceAll("[ ]*["+string+"]+[ ]*", string);
			}
			return str.replaceAll("[ ]+", " ").trim();
		}
		return str;
	}

	/**
	 * @param value
	 * @param replace
	 * @return
	 */
	public String replaceAllValue(String value, String replace) {
		try {
			if(!StringUtils.isEmpty(replace) && value != null) {
				String[] arr = replace.split("\\|");
				for (int i = 0; i< arr.length; i++) {
					value = value.toLowerCase().replace(arr[i].toLowerCase(), "");
				}
			}
		} catch (Exception e) {
//			LOGGER.error("replace: {}", e.getMessage());
		}
		return value;
	}

	public Map<String, String> getPageIndex(String str) {
		Map<String, String> map = new HashedMap<String, String>();
		
		try {
			for (int i = 1; i < 50; i++) {
				if(str.indexOf("<page"+i+">") != -1) {
					int start = str.indexOf("<page"+i+">");
					int end = str.indexOf("</page"+i+">");
					String endStr = "</page"+i+">";
					map.put("page"+i, start+"|"+(end+endStr.length()));
				} else {
					break;
				}
			}
		} catch (Exception e) {
//			LOGGER.error("getPageIndex: {}", e.getMessage());
		}
		
		return map;
	}
	
	public String getPageFromString(Map<String, String> pageIndex, Map<String, Integer> mapSaveIndexCuts, Map<String, Integer> mapSaveIndex) {
		try {
			int startCuts = mapSaveIndexCuts.get("start") !=null && mapSaveIndexCuts.get("start") != -1?mapSaveIndexCuts.get("start")+mapSaveIndexCuts.get("saveLengthStart"):0;
			int index =  startCuts + mapSaveIndex.get("start");
			if(index != -1) {
				for (Entry<String, String> entry : pageIndex.entrySet()) {
					String[] arr = entry.getValue().split("\\|");
					if(Integer.valueOf(arr[0]) <= index && Integer.valueOf(arr[1]) >= index) {
						return entry.getKey();
					}
				}
			}
		} catch (Exception e) {
//			LOGGER.error("getPageFromString: {}", e.getMessage());
		}
		
		return null;
	}
	
	public String cleanValue(String value) {
		String[] listCharTrims = {":", ";", ","};
		try {
			if(value != null) {
				value = value.replace(breakReplace.trim(), " ").replaceAll("[ ]+", " ").trim();
				for (String string : listCharTrims) {
					value = value.replaceAll("["+string+"]+", string+" ");
				}
				
				return value;
			}
		} catch (Exception e) {
//			LOGGER.error("cleanValue: {}", e.getMessage());
		}
		return value;
	}
	/**
	 * @param value
	 * @param f
	 * @param json
	 * @param params 
	 * @param namPage 
	 * @return
	 */
	public void convertTypeValue(String value, FieldTemplate f, JSONObject json, Params params, String namPage) {
		try {
			if(!StringUtils.isEmpty(f.getType()) && f.getType().equals("number") && value != null) {
				value = cleanValue(value);
				value = value.replaceAll("[^0-9]+", "");
				json.put(f.getName(), changeTypeValueReturn(value, params, namPage));
			} else if(!StringUtils.isEmpty(f.getType()) && f.getType().equals("dateString") && value != null) {
				value = cleanValue(value);
				value = value.replaceAll("[^0-9]+", " ").trim().replaceAll(" ", "/").trim();
				if(!StringUtils.isEmpty(f.getFormatDate()) && !StringUtils.isEmpty(f.getFormatDateTo())) {
					try {
						SimpleDateFormat dateFormat = new SimpleDateFormat(f.getFormatDate());
						SimpleDateFormat dateFormatTo = new SimpleDateFormat(f.getFormatDateTo());
						
						value = dateFormatTo.format(dateFormat.parse(value));
					} catch (Exception e) {
					}
				} else {
					try {
						SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
						SimpleDateFormat dateFormatTo = new SimpleDateFormat("dd/MM/yyyy");
						
						value = dateFormatTo.format(dateFormat.parse(value));
					} catch (Exception e) {
					}
				}
				json.put(f.getName(), changeTypeValueReturn(value, params, namPage));
			} else if(!StringUtils.isEmpty(f.getType()) && f.getType().equals("table") && value != null) {
				JSONObject table = handlingTable1(value, params);
				
				json.put(f.getName(), changeTypeValueReturn(table, params, namPage));
			} else if(!StringUtils.isEmpty(f.getType()) && f.getType().equals("table1") && value != null) {
				JSONObject table = handlingTable(value, params, f);
//				System.out.println(table.toString());
				json.put(f.getName(), changeTypeValueReturn(table, params, namPage));
			} else {
				value = cleanValue(value);
				
				json.put(f.getName(), value!=null?changeTypeValueReturn(value, params, namPage) :changeTypeValueReturn("", params, namPage));
			}
		} catch (Exception e) {
//			LOGGER.error("convertTypeValue: {}", e.getMessage());
		}
	}
	
	public Object changeTypeValueReturn(Object object, Params params, String namPage) {
		if(params != null && !StringUtils.isEmpty(params.getDanhDauTrang()) && params.getDanhDauTrang().equals("1")) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("value", object);
			jsonObject.put("page", namPage);
			
			return jsonObject;
		} else {
			return object;
		}
	}
	
	/**
	 * @param value
	 * @param params 
	 * @return
	 */
	public JSONObject handlingTable1(String value, Params params) {
		try {
			if(params.getTenFile().equals("pdf")) {
				String base64Pdf = params.getNoiDungFile();
				InputStream targetStream = new ByteArrayInputStream(Base64.getDecoder().decode(base64Pdf));
				PDDocument document = PDDocument.load(targetStream);
				PDFRenderer pdfRenderer = new PDFRenderer(document);
				
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				LOGGER.info("Pdf page: {}", document.getNumberOfPages());
				JSONArray jsonArray = new JSONArray();
				for (int page = 0; page < document.getNumberOfPages(); ++page)
				{ 
					BufferedImage bim = pdfRenderer.renderImage(page, 1F);
				    if(bim.getWidth() < 1300) {
				    	bim = pdfRenderer.renderImage(page, 2F);
				    }
				    baos = new ByteArrayOutputStream();
				    ImageIO.write(bim,"jpg", baos);
				    
				    String base64Img = Base64.getEncoder().encodeToString(baos.toByteArray());
				    params.setNoiDungFile(base64Img);
				    
				    String text = handlingTableFromImg(value, params);
				    JSONObject jsonObjectParse = new JSONObject(text);
				    JSONArray arrayTables = jsonObjectParse.getJSONArray("Tables");
				    for (int i = 0; i < arrayTables.length(); i++) {
				    	JSONObject object = new JSONObject();
				    	object.put("TableJson", new JSONObject(arrayTables.getJSONObject(i).getString("TableJson")));
				    	jsonArray.put(object);
					}
				}
				document.close();
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("Tables", jsonArray);
				return jsonObject;
			} else {
				String text = handlingTableFromImg(value, params);
				JSONObject jsonObjectParse = new JSONObject(text);
				
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("Tables", new JSONArray(jsonObjectParse.getString("Tables")));
				return jsonObject;
			}
		} catch (Exception e) {
//			LOGGER.error("handlingTable1: {}", e.getMessage());
		}
		return null;
	}
	private String handlingTableFromImg(String value, Params params) {
		try {
			int timeOutRequest = 15000;
			RequestConfig.Builder requestConfig = RequestConfig.custom();
			requestConfig.setConnectTimeout(timeOutRequest);
			requestConfig.setConnectionRequestTimeout(timeOutRequest);
			requestConfig.setSocketTimeout(timeOutRequest);
			
			byte[] byteImage = Base64.getDecoder().decode(params.getNoiDungFile());
			CloseableHttpClient httpClient = HttpClients.createDefault();
			
			HttpPost uploadFile = new HttpPost("https://trigger.extracttable.com/");
			uploadFile.setConfig(requestConfig.build());

			MultipartEntityBuilder builder = MultipartEntityBuilder.create();
			
			// This attaches the file to the POST:
			if(params.getFileOcr() != null) {
				builder.addBinaryBody("input", new File(params.getFileOcr()), ContentType.MULTIPART_FORM_DATA, "abc.jpg" );
			} else {
			builder.addBinaryBody("input", byteImage, ContentType.MULTIPART_FORM_DATA, "abc.jpg" );
			}
			
			HttpEntity multipart = builder.build();
			uploadFile.setEntity(multipart);
			CloseableHttpResponse response = httpClient.execute(uploadFile);
			HttpEntity responseEntity = response.getEntity();
			String text = IOUtils.toString(responseEntity.getContent(), StandardCharsets.UTF_8.name());
			System.out.println(text);
			
			return text;
			
		} catch (Exception e) {
//			LOGGER.error("handlingTable1: {}", e.getMessage());
		}
		return null;
	}
	/**
	 * @param value
	 * @param params 
	 * @param f 
	 * @return
	 */
	public JSONObject handlingTable(String value, Params params) {
		return handlingTable(value, params, null);
	}
	
	public JSONObject handlingTable(String value, Params params, FieldTemplate f) {
		try {
			int startInt = value.indexOf("<csv>", 0);
			int endInt = value.indexOf("</csv>", 0);
			JSONObject tables = new JSONObject();
			JSONArray jsonArray = new JSONArray();
			while (startInt >= 0) {
				try {
					String strTable = value.substring(startInt+5, endInt);
					String [] arrRow = strTable.split("<break>");
					JSONObject jsonObject = new JSONObject();
					JSONObject tableJson = new JSONObject();
					
					for (int i = 0; i < arrRow.length; i++) {
						if(khongThoaManDieuKienTable(f, arrRow[i])) continue;
						
						String[] arr = arrRow[i].split("\\|");
						JSONObject rowJson = new JSONObject();
						for (int j = 0; j < arr.length; j++) {
							if(khongPhaiCotChiDinh(f, j)) continue;
								rowJson.put(j+"", convertBreak(arr[j].trim()));
						}
						jsonObject.put(i+"", rowJson);
					}
					tableJson.put("TableJson", jsonObject);
					jsonArray.put(tableJson);
					
					startInt = value.indexOf("<csv>", startInt + "<csv>".length());
					endInt = value.indexOf("</csv>", endInt + "</csv>".length());
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
			tables.put("Tables", jsonArray);
			return tables;
		} catch (Exception e) {
//			LOGGER.error("handlingTable: {}", e.getMessage());
		}
		return null;
	}
	
	private boolean khongPhaiCotChiDinh(FieldTemplate f, int j) {
		try {
			if(f != null && !StringUtils.isEmpty(f.getEnd())) {
				String column = String.valueOf(j);
				String[] arrEnd = f.getEnd().split("\\|");
				List<String> al = new ArrayList<>();
				for (String string : arrEnd) {
					String str = string.replaceAll("[^0-9]+", "").trim();
					if(!StringUtils.isEmpty(str))
						al.add(str);
				}
				if(al.contains(column)) return false;
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	private String convertBreak(String trim) {
		try {
			trim = trim.replace(breakReplace.trim(), " ").replaceAll("[ ]+", " ").trim();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return trim;
	}

	public JSONArray handlingTablePage(String value, String page) {
		try {
			int startInt = value.indexOf("<csv>", 0);
			int endInt = value.indexOf("</csv>", 0);
			JSONArray jsonArray = new JSONArray();
			while (startInt >= 0) {
				try {
					String strTable = value.substring(startInt+5, endInt);
					String [] arrRow = strTable.split("<break>");
					JSONObject jsonObject = new JSONObject();
					JSONObject tableJson = new JSONObject();
					
					for (int i = 0; i < arrRow.length; i++) {
						String[] arr = arrRow[i].split("\\|");
						JSONObject rowJson = new JSONObject();
						for (int j = 0; j < arr.length; j++) {
							rowJson.put(j+"", arr[j].trim());
						}
						jsonObject.put(i+"", rowJson);
					}
					jsonObject.put("page", page);
					tableJson.put("TableJson", jsonObject);
					jsonArray.put(tableJson);
					
					startInt = value.indexOf("<csv>", startInt + "<csv>".length());
					endInt = value.indexOf("</csv>", endInt + "</csv>".length());
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
			return jsonArray;
		} catch (Exception e) {
//			LOGGER.error("handlingTable: {}", e.getMessage());
		}
		return null;
	}
	
	private boolean khongThoaManDieuKienTable(FieldTemplate f, String arrRow) {
		try {
			if(f != null && !StringUtils.isEmpty(f.getStart())) {
				String[] arrStart = f.getStart().split("\\|");
				arrStart = sortkeyword(arrStart);
				for (int i = 0; i < arrStart.length; i++) {
					String strP = getStrPatter(Utils.alias(arrRow), "("+Utils.alias(arrStart[i])+")");
					if(strP != null) return false;
				}
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	/**
	 * @param value
	 * @param f
	 * @return
	 */
	public String replaceAll(String value, FieldTemplate f) {
		try {
			if(!StringUtils.isEmpty(f.getReplaceAll()) && value != null) {
				String[] arr = f.getReplaceAll().split("\\|");
				for (int i = 0; i< arr.length; i++) {
					value = value.replace(breakReplace.trim(), " ").replaceAll("[ ]+", " ").trim();
					value = value.replaceAll(arr[i], "");
				}
			}
		} catch (Exception e) {
//			LOGGER.error("replaceAll: {}", e.getMessage());
		}
		return value;
	}

	/**
	 * @param value
	 * @param f
	 * @return
	 */
	public String replace(String value, FieldTemplate f) {
		try {
			if(!StringUtils.isEmpty(f.getReplace()) && value != null) {
				String[] arr = f.getReplace().split("\\|");
				for (int i = 0; i< arr.length; i++) {
					value = value.replace(arr[i], "");
				}
			}
		} catch (Exception e) {
//			LOGGER.error("replace: {}", e.getMessage());
		}
		return value;
	}

	/**
	 * @param f
	 * @param saveValue 
	 * @return
	 */
	public String split(FieldTemplate f, String saveValue) {
		String value = saveValue;
		try {
			if(f.getSplit() != null) {
				if(f.getSplit().getIndex() == -1) {
					String[] arr = saveValue.split(f.getSplit().getReg());
					value = saveValue != null?arr[arr.length-1]:null;
				} else {
					value = saveValue != null?saveValue.split(f.getSplit().getReg())[f.getSplit().getIndex()]:null;
				}
			}
		} catch (Exception e) {
//			LOGGER.error("split: {}", e.getMessage());
		}
		return value;
	}

	/**
	 * @param strHandling
	 * @param f
	 * @param mapSaveIndex 
	 * @return
	 */
	public String cutStartEnd(String strHandling, FieldTemplate f, Map<String, Integer> mapSaveIndex) {
		String value = null;
		try {
			if(!StringUtils.isEmpty(f.getStart()) && !StringUtils.isEmpty(f.getEnd()) ) {
				value = substringBetween(strHandling, f.getStart(), f.getEnd(), mapSaveIndex);
			} else if(!StringUtils.isEmpty(f.getStart()) && !StringUtils.isEmpty(f.getLine())) {
				value = substringBetweenBreakReplaceStart(strHandling, f.getStart(), breakReplace, f.getLine(), mapSaveIndex);
			} else if(!StringUtils.isEmpty(f.getEnd()) && !StringUtils.isEmpty(f.getLine())) {
				value = substringBetweenBreakReplaceEnd(strHandling, breakReplace, f.getEnd(), f.getLine(), mapSaveIndex);
			}  else if(!StringUtils.isEmpty(f.getStart())) {
				int startNextKeyword = 0;
				value = substringBetweenStart(strHandling, f.getStart(), mapSaveIndex, startNextKeyword);
			} else if(!StringUtils.isEmpty(f.getEnd())) {
				value = substringBetweenEnd(strHandling, f.getEnd(), mapSaveIndex);
			} else {
				value = strHandling;
			}
		} catch (Exception e) {
//			LOGGER.error("cutStartEnd: {}", e.getMessage());
		}
		return value;
	}

	/**
	 * @param strHandling
	 * @param f
	 * @param mapSaveIndex 
	 * @return
	 */
	public String cutString(String strHandling, FieldTemplate f, Map<String, Integer> mapSaveIndex) {
		try {
			if(f.getCuts() != null) {
				for (CutTemplate cut : f.getCuts()) {
					String strHandlingSave = strHandling;
					if(!StringUtils.isEmpty(cut.getStart()) && !StringUtils.isEmpty(cut.getEnd()) ) {
						strHandling = substringBetween(strHandling, cut.getStart(), cut.getEnd(), mapSaveIndex);
					} else if(!StringUtils.isEmpty(cut.getStart()) && !StringUtils.isEmpty(cut.getLine())) {
						strHandling = substringBetweenBreakReplaceStart(strHandling, cut.getStart(), breakReplace, cut.getLine(), mapSaveIndex);
					} else if(!StringUtils.isEmpty(cut.getEnd()) && !StringUtils.isEmpty(cut.getLine())) {
						strHandling = substringBetweenBreakReplaceEnd(strHandling, breakReplace, cut.getEnd(), cut.getLine(), mapSaveIndex);
					} else if(!StringUtils.isEmpty(cut.getStart())) {
						int startNotNextKeyword = 1;
						strHandling = substringBetweenStart(strHandling, cut.getStart(), mapSaveIndex, startNotNextKeyword);
					} else if(!StringUtils.isEmpty(cut.getEnd())) {
						strHandling = substringBetweenEnd(strHandling, cut.getEnd(), mapSaveIndex); 
					}
					if(StringUtils.isEmpty(strHandling)) {
						strHandling = strHandlingSave;
					}
				}
			}
		} catch (Exception e) {
//			LOGGER.error("cutString: {}", e.getMessage());
		}
		return strHandling;
	}

	public String cutStringNull(String strHandling, FieldTemplate f, Map<String, Integer> mapSaveIndex) {
		try {
			if(f.getCuts() != null) {
				for (CutTemplate cut : f.getCuts()) {
					mapSaveIndex.put("start", -1);
					mapSaveIndex.put("end", -1);
					
					if(!StringUtils.isEmpty(cut.getStart()) && !StringUtils.isEmpty(cut.getEnd()) ) {
						strHandling = substringBetween(strHandling, cut.getStart(), cut.getEnd(), mapSaveIndex);
					} else if(!StringUtils.isEmpty(cut.getStart()) && !StringUtils.isEmpty(cut.getLine())) {
						strHandling = substringBetweenBreakReplaceStart(strHandling, cut.getStart(), breakReplace, cut.getLine(), mapSaveIndex);
					} else if(!StringUtils.isEmpty(cut.getEnd()) && !StringUtils.isEmpty(cut.getLine())) {
						strHandling = substringBetweenBreakReplaceEnd(strHandling, breakReplace, cut.getEnd(), cut.getLine(), mapSaveIndex);
					} else if(!StringUtils.isEmpty(cut.getStart())) {
						int startNotNextKeyword = 1;
						strHandling = substringBetweenStart(strHandling, cut.getStart(), mapSaveIndex, startNotNextKeyword);
					} else if(!StringUtils.isEmpty(cut.getEnd())) {
						strHandling = substringBetweenEnd(strHandling, cut.getEnd(), mapSaveIndex); 
					}
				}
			}
		} catch (Exception e) {
//			LOGGER.error("cutString: {}", e.getMessage());
		}
		return strHandling;
	}
	
	private String substringBetween(String str, String startStr, String endStr, Map<String, Integer> mapSaveIndex) {
		try {
			String strAlias = Utils.alias(str);
			
			getIndexStart(strAlias, startStr, mapSaveIndex);
			
			getIndexEnd(strAlias, endStr, mapSaveIndex);
			
			if(mapSaveIndex.get("start") != -1 && mapSaveIndex.get("end") != -1) {
				return str.substring(mapSaveIndex.get("start") + mapSaveIndex.get("saveLengthStart"), mapSaveIndex.get("end")).trim();
			}
		} catch (Exception e) {
//			LOGGER.error("substringBetween: {}", e.getMessage());
		}
		
		return null;
	}
	
	/**
	 * @param strAlias
	 * @param startStr
	 * @param mapSaveIndex
	 */
	private void getIndexEnd(String strAlias, String endStr, Map<String, Integer> mapSaveIndex) {
		String[] arrEnd = endStr.split("\\|");
		arrEnd = sortkeyword(arrEnd);
		int cutStart = mapSaveIndex.get("start")+mapSaveIndex.get("saveLengthStart");
		for (int i = 0; i < arrEnd.length; i++) {
			try {
				String strP = getStrPatter(strAlias, "("+Utils.alias(arrEnd[i])+")");
				int endnext = strAlias.indexOf(strP);
				
				if((endnext != -1 && mapSaveIndex.get("end") == -1 && endnext >= cutStart) || (endnext != -1 && endnext < mapSaveIndex.get("end")  && endnext > cutStart)) {
					mapSaveIndex.put("end", endnext);
				}
			} catch (Exception e) {
			}
		}
		
	}
	
	/**
	 * @param strAlias
	 * @param endStr
	 * @param mapSaveIndex
	 * @param line
	 */
	private void getIndexEnd(String strAlias, String endStr, Map<String, Integer> mapSaveIndex, Integer line) {
		String[] arrEnd = endStr.split("\\|");
		arrEnd = sortkeyword(arrEnd);
		int cutStart = mapSaveIndex.get("start") + mapSaveIndex.get("saveLengthStart");
		for (int i = 0; i < arrEnd.length; i++) {
			try {
				String strP = getStrPatter(strAlias, "("+Utils.alias(arrEnd[i])+")");
				String strAliasNext = strAlias.substring(cutStart);
				int endnext = strAliasNext.indexOf(strP)+cutStart;
				if(line != null && line > 1) {
					for (int j = 1; j <= line; j++) {
						strAliasNext = strAlias.substring(endnext);
						int endnextSearch = strAliasNext.indexOf(strP);
						endnext = endnext+endnextSearch+strP.length();
					}
				}
				if((endnext != -1 && mapSaveIndex.get("end") == -1 && endnext >= cutStart) || (endnext != -1 && endnext < mapSaveIndex.get("end")  && endnext > cutStart)) {
					mapSaveIndex.put("end", endnext);
				}
			} catch (Exception e) {
//				LOGGER.error("substringBetweenBreakReplace: {}", e.getMessage());
			}
		}
		
	}
	
	/**
	 * @param strAlias
	 * @param endStr
	 * @param mapSaveIndex
	 */
	private void getIndexEndNotCheckStart(String strAlias, String endStr, Map<String, Integer> mapSaveIndex) {
		
		String[] arrEnd = endStr.split("\\|");
		arrEnd = sortkeyword(arrEnd);
		for (int i = 0; i < arrEnd.length; i++) {
			try {
				String strP = getStrPatter(strAlias, "("+Utils.alias(arrEnd[i])+")");
				int endnext = strAlias.indexOf(strP);
				
				if((endnext != -1 && mapSaveIndex.get("end") == -1) || (endnext != -1 && endnext < mapSaveIndex.get("end"))) {
					mapSaveIndex.put("end", endnext);
				}
			} catch (Exception e) {
			}
		}
		
	}
	public void getIndexStart(String strAlias, String startStr, Map<String, Integer> mapSaveIndex) {
		String[] arrStart = startStr.split("\\|");
		arrStart = sortkeyword(arrStart);
		for (int i = 0; i < arrStart.length; i++) {
			try {
				String strP = null;
				int startNext = -1;
				if(arrStart[i].indexOf(checkLastIndexKeyword) != -1) {
					strP = getStrPatterEnd(strAlias, "("+Utils.alias(arrStart[i].replace(checkLastIndexKeyword, ""))+")");
					startNext = strAlias.lastIndexOf(strP);
					if((startNext != -1 && mapSaveIndex.get("start") == -1) || (startNext != -1 && startNext > mapSaveIndex.get("start"))) {
						mapSaveIndex.put("saveLengthStart", strP.length());
						mapSaveIndex.put("start", startNext);
					}
				} else {
					strP = getStrPatter(strAlias, "("+Utils.alias(arrStart[i])+")");
					startNext = strAlias.indexOf(strP);
					if((startNext != -1 && mapSaveIndex.get("start") == -1) || (startNext != -1 && startNext < mapSaveIndex.get("start"))) {
						mapSaveIndex.put("saveLengthStart", strP.length());
						mapSaveIndex.put("start", startNext);
					}
				}
				
			} catch (Exception e) {
			}
		}
		
	}
	
	public void getIndexStart(String strAlias, String startStr, Map<String, Integer> mapSaveIndex, Integer line) {
		String[] arrStart = startStr.split("\\|");
		arrStart = sortkeyword(arrStart);
		mapSaveIndex.put("saveLengthStart", 0);
		for (int i = 0; i < arrStart.length; i++) {
			try {
				String strP = getStrPatterEnd(strAlias, "("+Utils.alias(arrStart[i])+")");
				String strAliasPrev = strAlias.substring(0, mapSaveIndex.get("end"));
				int endPrev = strAliasPrev.lastIndexOf(strP);
				if(line != null && line > 1) {
					for (int j = 1; j < line; j++) {
						strAliasPrev = strAlias.substring(0, endPrev);
						int endnextSearch = strAliasPrev.lastIndexOf(strP);
						endPrev = endnextSearch;
					}
				}
				
				int startNext = endPrev;
				if((startNext != -1 && mapSaveIndex.get("start") == -1) || (startNext != -1 && startNext < mapSaveIndex.get("start"))) {
					mapSaveIndex.put("start", startNext);
					mapSaveIndex.put("saveLengthStart", strP.length());
				}
			} catch (Exception e) {
			}
		}
	}
	public String[] sortkeyword(String[] arr) {
		Arrays.sort(arr, (a, b)->Integer.compare(b.length(), a.length()));
		
		return arr;
	}
	private String substringBetweenBreakReplaceEnd(String str, String startStr, String endStr, String line, Map<String, Integer> mapSaveIndex) {
		try {
			String strAlias = Utils.alias(str);
			
			getIndexEndNotCheckStart(strAlias, endStr, mapSaveIndex);
			
			if(line.indexOf("-") != -1) {
				String[] arrLine = line.split("-");
				getIndexStart(strAlias, startStr, mapSaveIndex, Integer.valueOf(arrLine[0]));
				mapSaveIndex.put("end", mapSaveIndex.get("start"));
				mapSaveIndex.put("start", -1);
				getIndexStart(strAlias, startStr, mapSaveIndex, (Integer.valueOf(arrLine[1]) - 1) );
			} else {
				getIndexStart(strAlias, startStr, mapSaveIndex, Integer.valueOf(line));
			}
			
			if(mapSaveIndex.get("start") != -1 && mapSaveIndex.get("end") != -1) {
				return str.substring(mapSaveIndex.get("start") + mapSaveIndex.get("saveLengthStart"), mapSaveIndex.get("end")).trim();
			}
		} catch (Exception e) {
//			LOGGER.error("substringBetweenBreakReplaceEnd: {}", e.getMessage());
		}
		
		return null;
	}
	
	private String substringBetweenBreakReplaceStart(String str, String startStr, String endStr, String line, Map<String, Integer> mapSaveIndex) {
		try {
			String strAlias = Utils.alias(str);
			
			getIndexStart(strAlias, startStr, mapSaveIndex);
			
			if(line.indexOf("-") != -1) {
				String[] arrLine = line.split("-");
				getIndexEnd(strAlias, endStr, mapSaveIndex, Integer.valueOf(arrLine[0]));
				mapSaveIndex.put("start", mapSaveIndex.get("end"));
				mapSaveIndex.put("saveLengthStart", 0);
				mapSaveIndex.put("end", -1);
				getIndexEnd(strAlias, endStr, mapSaveIndex, (Integer.valueOf(arrLine[1]) - 1) );
			} else {
				getIndexEnd(strAlias, endStr, mapSaveIndex, Integer.valueOf(line));
			}
			
			
			if(mapSaveIndex.get("start")  != -1 && mapSaveIndex.get("end") != -1) {
				return str.substring(mapSaveIndex.get("start") + mapSaveIndex.get("saveLengthStart"), mapSaveIndex.get("end")).trim();
			}
		} catch (Exception e) {
//			LOGGER.error("substringBetweenBreakReplace: {}", e.getMessage());
		}
		
		return null;
	}
	
	private String substringBetweenEnd(String str, String endStr, Map<String, Integer> mapSaveIndex) {
		try {
			String strAlias = Utils.alias(str);
			
			mapSaveIndex.put("start", 0);
			
			getIndexEndNotCheckStart(strAlias, endStr, mapSaveIndex);
			
			if(mapSaveIndex.get("start") != -1 && mapSaveIndex.get("end") != -1) {
				return str.substring(mapSaveIndex.get("start"), mapSaveIndex.get("end")).trim();
			}
		} catch (Exception e) {
//			LOGGER.error("substringBetweenEnd: {}", e.getMessage());
		}
		
		return null;
	}
	
	private String substringBetweenStart(String str, String startStr, Map<String, Integer> mapSaveIndex, int type) {
		try {
			String strAlias = Utils.alias(str);
			
			getIndexStart(strAlias, startStr, mapSaveIndex);
			
			if(mapSaveIndex.get("start") != -1) {
				int startNotNextKeyword = 1;
				if(type == startNotNextKeyword) {
					return str.substring(mapSaveIndex.get("start") + mapSaveIndex.get("saveLengthStart")).trim();
				} else {
					mapSaveIndex.put("end", -1);
					getIndexEnd(strAlias, breakReplace, mapSaveIndex, 1);
					
					String getStr = "";
					if(mapSaveIndex.get("start")  != -1 && mapSaveIndex.get("end") != -1) {
						getStr = str.substring(mapSaveIndex.get("start") + mapSaveIndex.get("saveLengthStart"), mapSaveIndex.get("end")).trim();
					}
					
					if(!StringUtils.isEmpty(cleanValue(getStr))) {
						if(getStr.indexOf(":") != -1) {
							return getStr.split(":")[0];
						} else {
							return getStr;
						}
					} else {
						mapSaveIndex.put("end", -1);
						getIndexEnd(strAlias, breakReplace, mapSaveIndex, 2);
						
						if(mapSaveIndex.get("start")  != -1 && mapSaveIndex.get("end") != -1) {
							getStr = str.substring(mapSaveIndex.get("start") + mapSaveIndex.get("saveLengthStart"), mapSaveIndex.get("end")).trim();
						}
						if(getStr.indexOf(":") != -1) {
							return getStr.split(":")[0];
						} else {
							return getStr;
						}
					}
				}
			}
		} catch (Exception e) {
		}
		
		return null;
	}
	
	public static String getStrPatter(String str, String pattern) {
		try {
			if(!StringUtils.isEmpty(str)) {
	 			Pattern r = Pattern.compile(pattern);
			    Matcher m = r.matcher(str);
				String strFind = null;
			    while  (m.find()) {
			    	return m.group(1).replaceAll("[\\s]+", " ").trim();
			    }
			    return strFind;
			}
		} catch (Exception e) {
//			LOGGER.error("getStrPatter: {}", e.getMessage());
		} 
	    return null;
	}
	
	public static String getStrPatterEnd(String str, String pattern) {
		try {
			if(!StringUtils.isEmpty(str)) {
	 			Pattern r = Pattern.compile(pattern);
			    Matcher m = r.matcher(str);
				String strFind = null;
			    while  (m.find()) {
			    	strFind = m.group(1).replaceAll("[\\s]+", " ").trim();
			    }
			    return strFind;
			}
		} catch (Exception e) {
//			LOGGER.error("getStrPatterEnd: {}", e.getMessage());
		} 
	    return null;
	}
}
