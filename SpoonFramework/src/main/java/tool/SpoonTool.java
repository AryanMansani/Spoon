package tool;

import spoon.Launcher;
import spoon.reflect.CtModel;
import spoon.reflect.declaration.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import javax.annotation.*;



public class SpoonTool {
    public static void main(String[] args) throws ClassNotFoundException, IOException {

        //Spoon Metamodel
        //Structural Code Reference

        Launcher launcher = new Launcher();
        launcher.addInputResource("C:/Users/mrary/Desktop/spring-multi-module-application-master/spring-multi-module-application-master");
        launcher.buildModel();
        CtModel model = launcher.getModel();

        System.out.println("Packages - ");

        for(CtPackage p : model.getAllPackages())
        {

            p.setSimpleName("newname."+p.getQualifiedName());
            System.out.println("package: " + p.getQualifiedName());

        }




        //System.out.println("------------------------------------------------");
//        System.out.println("Classes - ");
//
//
//
//        for(CtType<?> s : model.getAllTypes())
//        {
//
//            System.out.println("class: " + s.getQualifiedName());
//            System.out.println("Methods -");
//            Set<CtMethod<?>> list = s.getMethods();
//
//            Iterator<spoon.reflect.declaration.CtMethod<?>> namesIterator = list.iterator();
//            Iterator<spoon.reflect.declaration.CtMethod<?>> it = list.iterator();
//
//            while(namesIterator.hasNext())
//            {
//
//                System.out.println("   "+namesIterator.next().<CtNamedElement>setSimpleName("new method")+"  Annotations - "+it.next().getAnnotations());
//            }
//            System.out.println("------------------------------------------------");
//
//        }
//






//        HashMap<String, ArrayList<String>> SubPackage = new HashMap<String, ArrayList<String>>();
//        HashMap<String,ArrayList<String>> Files = new HashMap<String,ArrayList<String>>();
//        HashMap<String,String> Path = new HashMap<String,String>();
//        listOfPackage("C:/Users/mrary/Desktop/spring-multi-module-application-master/spring-multi-module-application-master/",SubPackage,Files,Path,"/","/");
//        //UI
//        UI(SubPackage,Files,Path);



    }

    public static void UI(HashMap<String,ArrayList<String>> SubPackage,HashMap<String,ArrayList<String>> Files, HashMap<String,String> Path) throws IOException {
        //UI
        ArrayList<String> Packages = new ArrayList<String>(SubPackage.size());
        System.out.println("Packages Present in the Project - ");
        for (Map.Entry<String,ArrayList<String>> entry : SubPackage.entrySet())
        {

            Packages.add(entry.getKey());

        }



        while(true)
        {

            System.out.println("Packages - ");
            for(int i=0;i<Packages.size();i++)
            {

                System.out.println(i+". "+Packages.get(i));

            }
            System.out.print("Enter Value - ");

            Scanner myObj = new Scanner(System.in);  // Create a Scanner object

            int val = myObj.nextInt();

            String packag = Packages.get(val);

            System.out.println("------------------------------------------------");

            while(true)
            {
                ArrayList<String> SubPack = SubPackage.get(packag);
                System.out.println("SubPackages - ");
                for(int i=0;i<SubPack.size();i++)
                {
                    System.out.println(i+". "+SubPack.get(i));
                }
                System.out.print("Enter Value - ");
                int val2 = myObj.nextInt();

                if(val2==-1)
                {
                    break;
                }

                String Sub = SubPack.get(val2);
                System.out.println("------------------------------------------------");

                while(true)
                {
                    ArrayList<String> Fil = Files.get(Sub);
                    System.out.println("Files - ");
                    for(int i=0;i<Fil.size();i++)
                    {
                        System.out.println(i+". "+Fil.get(i));
                    }
                    System.out.print("Enter Value - ");
                    int val3 = myObj.nextInt();

                    if(val3==-1)
                    {
                        break;
                    }

                    String fl = Fil.get(val3);
                    String pth = Path.get(fl);
                    String code = getCode(pth);

                    CtClass l = Launcher.parseClass(code);

                    System.out.println("------------------------------------------------");




                    while(true)
                    {
                        Set<CtMethod<?>> list = l.getMethods();

                        Iterator<spoon.reflect.declaration.CtMethod<?>> namesIterator = list.iterator();
                        Iterator<spoon.reflect.declaration.CtMethod<?>> it = list.iterator();

                        System.out.println("0. "+"Methods");
                        System.out.println("1. "+"Annotations");

                        int val4 = myObj.nextInt();
                        System.out.println("------------------------------------------------");

                        if(val4==0)
                        {
                            System.out.println("Methods -");
                            while(namesIterator.hasNext())
                            {

                                System.out.println("   "+namesIterator.next().getSimpleName()+"  Annotations - "+it.next().getAnnotations());
                            }

                            int val5 = myObj.nextInt();
                            System.out.println("------------------------------------------------");

                        }
                        else if(val4==1)
                        {
                            System.out.println("Annotations - ");
                            System.out.println("   "+l.getAnnotations());
                            int val6 = myObj.nextInt();
                            System.out.println("------------------------------------------------");
                        }
                        else
                        {
                            break;
                        }


                    }







                }

            }




        }


    }

    public static String getCode(String path) throws IOException {


        File file = new File(path);
        BufferedReader br = new BufferedReader(new FileReader(file));
        String ans="";
        String st;
        while ((st = br.readLine()) != null)
        {
            ans+=("\r\n"+st);
        }

        return ans;
    }


    public static void listOfPackage(String directoryName,HashMap<String,ArrayList<String>> SubPackage,HashMap<String,ArrayList<String>> Files, HashMap<String,String> Path,String p,String root) {

        File directory = new File(directoryName);
        File[] fList = directory.listFiles();

        for (File file : fList)
        {



            if (file.isFile())
            {

                String path=file.getPath();

                //Package to SubPackage Connection
                if(root!=p)
                {
                    if (!SubPackage.containsKey(root))
                    {
                        SubPackage.put(root, new ArrayList<String>());
                    }
                    SubPackage.get(root).add(p);
                }


                //SubPackage to File Connection
                if (!Files.containsKey(p))
                {
                    Files.put(p, new ArrayList<String>());
                }
                Files.get(p).add(file.getName());

                //File to Path Connection
                Path.put(file.getName(),file.getPath().replace('\\','/'));

            }
            else if (file.isDirectory())
            {
                if(p=="/")
                {
                    root = "."+file.getName();
                }
                String temp =p + "."+file.getName();
                listOfPackage(file.getAbsolutePath(),SubPackage,Files,Path,temp,root);
            }
        }
    }
}
