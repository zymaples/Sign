package com.sign;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.AcroFields.Item;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

public class PDFUtils {


    /**
     * @param fields
     * @param data
     * @throws IOException
     * @throws DocumentException
     */
    private static void fillData(AcroFields fields, Map<String, String> data) throws IOException, DocumentException {
        List<String> keys = new ArrayList<String>();
        Map<String, Item> formFields = fields.getFields();
        for (String key : data.keySet()) {
            if(formFields.containsKey(key)){
                String value = data.get(key);
                fields.setField(key, value); // 为字段赋值,注意字段名称是区分大小写的
                keys.add(key);
            }
        }
        Iterator<String> itemsKey = formFields.keySet().iterator();
        while(itemsKey.hasNext()){
            String itemKey = itemsKey.next();
            if(!keys.contains(itemKey)){
                fields.setField(itemKey, " ");
            }
        }
    }

    /**
     * @param templatePdfPath
     *            模板pdf路径
     * @param generatePdfPath
     *            生成pdf路径
     * @param data
     *            数据
     */
    public static String generatePDF(String templatePdfPath, String generatePdfPath, Map<String, String> data) {
        OutputStream fos = null;
        ByteArrayOutputStream bos = null;
        try {
            PdfReader reader = new PdfReader(templatePdfPath);
            bos = new ByteArrayOutputStream();
            /* 将要生成的目标PDF文件名称 */
            PdfStamper ps = new PdfStamper(reader, bos);
            /* 使用中文字体 */
            BaseFont bf = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H",BaseFont.NOT_EMBEDDED);
            ArrayList<BaseFont> fontList = new ArrayList<BaseFont>();
            fontList.add(bf);
            /* 取出报表模板中的所有字段 */
            AcroFields fields = ps.getAcroFields();
            fields.setSubstitutionFonts(fontList);
            fillData(fields, data);
            /* 必须要调用这个，否则文档不会生成的  如果为false那么生成的PDF文件还能编辑，一定要设为true*/
            ps.setFormFlattening(true);
            ps.close();
            fos = new FileOutputStream(generatePdfPath);
            fos.write(bos.toByteArray());
            fos.flush();
            return generatePdfPath;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public static void main(String[] args) {
        Map<String, String> data = new HashMap<String, String>();
        //key为pdf模板的form表单的名字，value为需要填充的值
        data.put("title", "在线医院");
        data.put("case", "123456789");
        data.put("date", "2018.12.07");
        data.put("name", "gitbook");
        data.put("sex", "男");
        data.put("age", "29");
        data.put("phone", "13711645814");
        data.put("office", "内科");
        data.put("cert", "身痒找打");
        data.put("drug", "1、奥美拉唑肠溶胶囊             0.25g10粒×2板 ");
        data.put("dose", "×2盒");
        data.put("cons", "用法用量：口服 一日两次 一次2粒");
        data.put("tips", "温馨提示");
        data.put("desc", "尽量呆在通风较好的地方，保持空气流通，有利于病情康复。尽量呆在通风较好的地方");
        generatePDF("E:\\CWCA\\Sign\\file\\tpl.pdf",
                "E:\\CWCA\\Sign\\file\\filled.pdf", data );
    }
}
