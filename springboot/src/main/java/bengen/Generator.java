package bengen;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import io.swagger.models.parameters.PathParameter;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.parser.OpenAPIV3Parser;
import io.swagger.v3.parser.core.models.SwaggerParseResult;
import org.apache.commons.io.FileUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Generator {
    public static void main(String[] args) {


        Map<String, Object> properties;
        ClassPathResource classPathResource = new ClassPathResource("swagger/api.yml");
        String filePath = "";
        try {
            String apiFileStr = FileUtils.readFileToString(classPathResource.getFile(), StandardCharsets.UTF_8).trim();

            SwaggerParseResult parseResult = new OpenAPIV3Parser().readContents(apiFileStr);

            String controllerPackage = "bengen.controller";
            String beanPackageName = "bengen.bean";
            generateContrller(parseResult, controllerPackage,beanPackageName);

            List<EnumModel> enumList = new ArrayList<>();

            String packagePath = "/bengen/bean";
            List<BeanModel> beanList = new ArrayList<>();
            Components components = parseResult.getOpenAPI().getComponents();
            int schemasCount = components.getSchemas().size();
            Map<String, Schema> classMap = components.getSchemas();

            for (String classkey : classMap.keySet()) {
                BeanModel bean = new BeanModel();
                bean.setPackageName(beanPackageName);
                bean.setPackagePath(packagePath);
                //todo
                //key first char cap;
                bean.setClassName(classkey);
                List<Attribute> attributes = new ArrayList<>();
                Map<String, Schema> attrMap = classMap.get(classkey).getProperties();
                for (String attrKey : attrMap.keySet()) {

                    Attribute att = new Attribute();
                    att.setName(attrKey);

                    // todo
                    // if type ==null has ref
                    if (attrMap.get(attrKey).getType() == null && attrMap.get(attrKey).get$ref() != null) {
                        String ref = attrMap.get(attrKey).get$ref();
                        String refType = ref.substring(ref.lastIndexOf('/') + 1);
                        att.setType(refType);
                    }
                    // check if enum exist
                    else if (attrMap.get(attrKey).getEnum() != null) {
                        String enumName = "Enum" + attrKey;
                        att.setType(enumName);
                        List<String> enumValues = attrMap.get(attrKey).getEnum();
                        if (!enumList.contains(attrKey)) {
                            EnumModel enumModel = new EnumModel();
                            enumModel.setPackageName(beanPackageName);
                            enumModel.setEnumName(enumName);
                            enumModel.setValues(enumValues);
                            enumList.add(enumModel);
                        }
                    } else {

                        //type transform
                        String jtype = TypeTransform.openAPIv3TypeToJava(attrMap.get(attrKey).getType());
                        att.setType(jtype);
                    }

                    attributes.add(att);

                }


                bean.setAttributeList(attributes);

                beanList.add(bean);

            }

//            generate(beanList);
//            generateEnum(enumList);

            System.out.println();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public static void generate(List<BeanModel> beans) {
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_31);
        File templateDir = null;
        Template temp = null;
        try {
            templateDir = ResourceUtils.getFile("classpath:template");
            configuration.setDirectoryForTemplateLoading(templateDir);
            configuration.setDefaultEncoding("UTF-8");
            configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
            temp = configuration.getTemplate("bean.ftl");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

//        String projectPath = System.getProperty("user.dir");

        for (BeanModel bean : beans) {

            String packagefullPath = "E:/workspace/openapi/springboot/src/main/java/bengen/bean";

            try {
                File dir = new File(packagefullPath);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                String fileName = bean.getClassName() + ".java";
                OutputStream fos = new FileOutputStream(new File(dir, fileName)); //java文件的生成目录
                Writer out = new OutputStreamWriter(fos);
                temp.process(bean, out);

                fos.flush();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (TemplateException e) {
                e.printStackTrace();
            }

        }
    }

    public static void generateEnum(List<EnumModel> enumModels) {
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_31);
        File templateDir = null;
        Template temp = null;
        try {
            templateDir = ResourceUtils.getFile("classpath:template");
            configuration.setDirectoryForTemplateLoading(templateDir);
            configuration.setDefaultEncoding("UTF-8");
            configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
            temp = configuration.getTemplate("enum.ftl");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String packagefullPath = "E:/workspace/openapi/springboot/src/main/java/bengen/bean";

        for (EnumModel bean : enumModels) {

            try {
                File dir = new File(packagefullPath);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                String fileName = bean.getEnumName() + ".java";
                OutputStream fos = new FileOutputStream(new File(dir, fileName)); //java文件的生成目录
                Writer out = new OutputStreamWriter(fos);
                temp.process(bean, out);

                fos.flush();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (TemplateException e) {
                e.printStackTrace();
            }

        }
    }

    public static void generateContrller(SwaggerParseResult parseResult, String controllerPackageName,String beanPackage) {
        ControllerModel controllerModel = new ControllerModel();
        String className = "";
        String basePath = "/";
        className = parseResult.getOpenAPI().getTags().get(0).getName() + "Controller";
        List<ControllerMethod> methods = new ArrayList<>();

        Paths paths = parseResult.getOpenAPI().getPaths();
        if (paths.size() > 0) {
            for (String key : paths.keySet()) {
                ControllerMethod method = new ControllerMethod();
                String path = key;
                String getOrPost = "";
                String methodName = "";
                String parameterType = "";
                String parameterName = "";
                PathItem item = paths.get(key);
                if (item.getGet() != null) {
                    getOrPost = "GetMapping";
                    methodName = item.getGet().getOperationId();
                    //parameter
                    if (item.getGet().getParameters() != null) {
                        Parameter parameter = item.getGet().getParameters().get(0);
                        parameterName = parameter.getName();
                        if (parameter.get$ref() != null) {
                            parameterType = parameter.get$ref().substring(parameter.get$ref().lastIndexOf('/') + 1);
                            parameterName = "p" + parameterType;
                        } else {
                            String type = parameter.getSchema().getType();
                            parameterType = TypeTransform.openAPIv3TypeToJava(type);
                        }
                    }

                } else if (item.getPost() != null) {
                    getOrPost = "PostMapping";
                    methodName = item.getPost().getOperationId();
                    //parameter
                    if (item.getPost().getRequestBody() != null) {
                        String ref = item.getPost().getRequestBody().getContent().get("application/json").getSchema().get$ref();
                        String type = item.getPost().getRequestBody().getContent().get("application/json").getSchema().getType();
                        if (ref != null) {
                            parameterType = ref.substring(ref.lastIndexOf('/') + 1);
                            parameterName = "p" + parameterType;
                        } else {
                            parameterType = TypeTransform.openAPIv3TypeToJava(type);
                        }
                    }
                }
                method.setGetOrPost(getOrPost);
                method.setMappingPath(path);
                method.setMethodName(methodName);
                method.setParameterName(parameterName);
                method.setParameterType(parameterType);
                methods.add(method);

            }
            controllerModel.setControllerName(className);
            controllerModel.setMethodList(methods);
            controllerModel.setControllerPackage(controllerPackageName);
            controllerModel.setBeanPackage(beanPackage);
            controllerModel.setRequestMapping(basePath);
        }

        Configuration configuration = new Configuration(Configuration.VERSION_2_3_31);
        File templateDir = null;
        Template temp = null;
        try {
            templateDir = ResourceUtils.getFile("classpath:template");
            configuration.setDirectoryForTemplateLoading(templateDir);
            configuration.setDefaultEncoding("UTF-8");
            configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
            temp = configuration.getTemplate("controller.ftl");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String packagefullPath = "E:/workspace/openapi/springboot/src/main/java/bengen/controller";

        try {
            File dir = new File(packagefullPath);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            String fileName = controllerModel.getControllerName() + ".java";
            OutputStream fos = new FileOutputStream(new File(dir, fileName)); //java文件的生成目录
            Writer out = new OutputStreamWriter(fos);
            temp.process(controllerModel, out);

            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TemplateException e) {
            e.printStackTrace();
        }

    }
}
