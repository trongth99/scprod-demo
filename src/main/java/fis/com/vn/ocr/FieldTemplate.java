/**
 * 
 */
package fis.com.vn.ocr;

import java.util.ArrayList;

import lombok.Data;

/**
 * @author ChinhVD4
 *
 */
@Data
public class FieldTemplate {
	String name;
	ArrayList<CutTemplate> cuts;
	SplitTemplate split;
	String type;
	String start;
	String end;
	String line;
	String formatDate;
	String formatDateTo;
	String replace;
	String replaceAll;
}
