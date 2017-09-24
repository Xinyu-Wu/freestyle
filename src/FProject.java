
import java.util.ArrayList;
import java.util.List;
import net.sf.json.JSONObject;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.UUID;
import java.io.File;
import java.io.FileOutputStream;
import java.io.*;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.Layer;
import org.geotools.map.MapContent;
import org.geotools.swing.data.JFileDataStoreChooser;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author 冯雨宁
 */
public class FProject {
    private String FName;//工程名
    //private int FLayerNum;//图层数
    private List<String> FLayerID;//图层索引列表
    private MapContent FMapContent;//图层类

    public MapContent getFMapContent() {

        return FMapContent;
    }

    public void setFMapContent(MapContent FMapContent) {
        this.FMapContent = FMapContent;
    }

    public void setFName(String FName) {
        this.FName = FName;
    }   

    public void setFLayerID(List<String> FLayerID) {
        this.FLayerID = FLayerID;
    }

    public int getFLayerNum() {
        return FLayerID.size();
    }

    public List<String> getFLayerID() {
        return FLayerID;
    }
    
    public String getFName()
    {
        return this.FName;
    }
    
    public String getFilePath()//默认保存在当前目录下，json格式，文件名为工程名
    {
        String path="./";
        String fileNameTemp = path+FName+".json";//文件路径+名称+文件类型
        return fileNameTemp;
    }
    
    
     public FProject(){
        FName="";
        FLayerID= new ArrayList<String>();
        FMapContent=new MapContent();
    }
    
    public FProject(String name){
        FName=name;
        FLayerID= new ArrayList<String>();
        FMapContent=new MapContent();
    }
      
    static FProject openFromFile(File file) throws FileNotFoundException, IOException{//从json文件打开
        BufferedReader fr = new BufferedReader(new FileReader(file));
        String json = fr.readLine();
        JSONObject jsonObject=JSONObject.fromObject(json);
        FProject fp=(FProject)JSONObject.toBean(jsonObject, FProject.class); 
        System.out.println("success open file,the file is "+fp.getFName());
        return fp;
    }
    
    public boolean saveFProject(){
        JSONObject jsonObject = JSONObject.fromObject(this);
        System.out.println(jsonObject);
          
        Boolean bool = false;
        String fileNameTemp = this.getFilePath();
        File file = new File(fileNameTemp);
        
        try {
            //如果文件不存在，则创建新的文件
            if(!file.exists()){
                file.createNewFile();
                System.out.println("success create file,the file is "+fileNameTemp);
                //创建文件成功后，写入内容到文件里
            }
            FileWriter fw = new FileWriter(file); 
            fw.write(jsonObject.toString());
            fw.close();
            System.out.println("success save file,the file is "+fileNameTemp);
            bool = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return bool;    
    }
    
    public void addLayer(String id, Layer mLayer){//先把图层存进数据库，再把数据库id作为参数传入
        FLayerID.add(id);
        FMapContent.addLayer(mLayer);
    }
    
    public void deleteLayer(int index){//先获取要删除图层的id，作为参数传入
   
        FLayerID.remove(index);
        FMapContent.layers().remove(index);
    }
      
    public void moveLayer(int sourcePos, int desPos){//移动图层顺序
        String id=FLayerID.get(sourcePos);
        FLayerID.remove(id);
        if(sourcePos>desPos)
        {
            FLayerID.add(desPos, id);
        }
        else
        {
            FLayerID.add(desPos-1,id);
        }
       FMapContent.moveLayer(sourcePos, desPos);
    }
    
    public static void main(String[] args) throws Exception{
        String ss="fb";      
        FProject fp = new FProject(ss);
        fp.saveFProject();
        
        File file = JFileDataStoreChooser.showOpenFile("json", null);
        if (file == null) {
            return;
        }
        
        FProject a=openFromFile(file);
        System.out.println(a.getFName());   
    }   
}
