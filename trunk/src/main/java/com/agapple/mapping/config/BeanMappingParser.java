package com.agapple.mapping.config;

import java.beans.PropertyDescriptor;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.agapple.mapping.BeanMappingException;
import com.agapple.mapping.helper.ReflectionHelper;
import com.agapple.mapping.helper.XmlHelper;

/**
 * 解析对应的mapping配置
 * 
 * @author jianghang 2011-5-26 下午07:13:45
 */
public class BeanMappingParser {

    private static final String MAPPING_SCHEMA = "META-INF/mapping.xsd";

    public static List<BeanMappingObject> parseMapping(InputStream in) throws BeanMappingException {
        List<BeanMappingObject> result = parseMappingObject(in);
        List<BeanMappingObject> reseverResult = new ArrayList<BeanMappingObject>(result.size());
        for (BeanMappingObject object : result) {
            if (object.isReversable()) {
                BeanMappingObject reverseObject = reverse(object);
                if (reverseObject != null) {
                    reseverResult.add(reverseObject);
                }
            }
        }
        result.addAll(reseverResult);
        return result;
    }

    public static List<BeanMappingObject> parseMapping(Class src, Class target) throws BeanMappingException {
        List<BeanMappingObject> result = new ArrayList<BeanMappingObject>(2);
        PropertyDescriptor[] targetPds = ReflectionHelper.getPropertyDescriptors(target);
        PropertyDescriptor[] srcPds = ReflectionHelper.getPropertyDescriptors(src);
        BeanMappingObject object = new BeanMappingObject();
        object.setSrcClass(src);
        object.setTargetClass(target);
        // object.setBatch(true);
        List<BeanMappingField> fields = new ArrayList<BeanMappingField>();
        for (PropertyDescriptor targetPd : targetPds) {
            String property = targetPd.getName();
            PropertyDescriptor srcPd = getMatchPropertyDescriptor(srcPds, property);
            if (srcPd == null) {// 没有匹配属性
                continue;
            }

            if (targetPd.getWriteMethod() != null && srcPd.getReadMethod() != null) {
                BeanMappingField field = new BeanMappingField();
                field.setSrcName(property);
                field.setTargetName(property);
                field.setTargetClass(targetPd.getPropertyType());
                field.setSrcClass(srcPd.getPropertyType());
                fields.add(field);
            }

        }
        object.setBeanFields(fields);
        result.add(object);
        if (object.isReversable()) {
            BeanMappingObject reverseObject = reverse(object);
            if (reverseObject != null) {
                result.add(reverseObject);
            }
        }
        return result;
    }

    public static List<BeanMappingObject> parseMapMapping(Class src) throws BeanMappingException {
        List<BeanMappingObject> result = new ArrayList<BeanMappingObject>(2);
        PropertyDescriptor[] targetPds = ReflectionHelper.getPropertyDescriptors(src);
        BeanMappingObject object = new BeanMappingObject();
        object.setSrcClass(src);
        object.setTargetClass(HashMap.class);
        object.setBatch(true);
        List<BeanMappingField> fields = new ArrayList<BeanMappingField>();
        for (PropertyDescriptor targetPd : targetPds) {
            if (targetPd.getWriteMethod() != null && targetPd.getReadMethod() != null) {
                BeanMappingField field = new BeanMappingField();
                field.setSrcName(targetPd.getName());
                field.setSrcClass(targetPd.getPropertyType());
                field.setTargetName(targetPd.getName());
                field.setTargetClass(targetPd.getPropertyType());
                fields.add(field);
            }

        }
        object.setBeanFields(fields);
        result.add(object);
        if (object.isReversable()) {
            BeanMappingObject reverseObject = reverse(object);
            if (reverseObject != null) {
                result.add(reverseObject);
            }
        }
        return result;
    }

    private static BeanMappingObject reverse(BeanMappingObject object) {
        BeanMappingObject newObject = new BeanMappingObject();
        // 反转一下属性，主要是一些srcName,srcClass等
        newObject.setSrcClass(object.getTargetClass());
        newObject.setTargetClass(object.getSrcClass());
        newObject.setReversable(object.isReversable());
        newObject.setBatch(object.isBatch());
        newObject.setSrcKey(object.getSrcKey());
        newObject.setTargetKey(object.getTargetKey());

        List<BeanMappingField> fields = newObject.getBeanFields();
        for (BeanMappingField field : object.getBeanFields()) {
            BeanMappingField newField = new BeanMappingField();
            newField.setSrcName(field.getTargetName());
            newField.setTargetName(field.getSrcName());
            newField.setSrcClass(field.getTargetClass());
            newField.setTargetClass(field.getSrcClass());
            newField.setDefaultValue(field.getDefaultValue());
            newField.setMapping(field.isMapping());
            if (StringUtils.isNotEmpty(field.getConvertor()) || StringUtils.isNotEmpty(field.getScript())) {
                object.setReversable(false);// 强制设置为false
                return null;
            }
            fields.add(newField);
        }
        return newObject;
    }

    // 根据属性名获取一下匹配的PropertyDescriptor
    private static PropertyDescriptor getMatchPropertyDescriptor(PropertyDescriptor[] srcPds, String property) {
        for (PropertyDescriptor srcPd : srcPds) {
            if (srcPd.getName().equals(property)) {
                return srcPd;
            }
        }

        return null;
    }

    // 解析一下bean-mapping
    private static List<BeanMappingObject> parseMappingObject(InputStream in) {
        Document doc = XmlHelper.createDocument(
                                                in,
                                                Thread.currentThread().getContextClassLoader().getResourceAsStream(
                                                                                                                   MAPPING_SCHEMA));
        Element root = doc.getDocumentElement();
        NodeList nodeList = root.getElementsByTagName("bean-mapping");
        List<BeanMappingObject> mappings = new ArrayList<BeanMappingObject>();
        // 解析BeanMappingObject属性
        for (int i = 0; i < nodeList.getLength(); i++) {
            BeanMappingObject config = new BeanMappingObject();
            Node node = nodeList.item(i);
            // mapping source class
            Node srcNode = node.getAttributes().getNamedItem("srcClass");
            // mapping target class
            Node targetNode = node.getAttributes().getNamedItem("targetClass");

            if (srcNode == null || targetNode == null) {
                throw new BeanMappingException("Parse error for bean-mapping srcClass or targetClass is null");
            }
            Node srcKeyNode = node.getAttributes().getNamedItem("srcKey");
            Node targetKeyNode = node.getAttributes().getNamedItem("targetKey");
            // 设置reversable
            Node reversableNode = node.getAttributes().getNamedItem("reversable");
            Node batchNode = node.getAttributes().getNamedItem("batch");

            String src = srcNode.getNodeValue();
            String target = targetNode.getNodeValue();
            config.setSrcClass(ReflectionHelper.forName(src));
            config.setTargetClass(ReflectionHelper.forName(target));
            if (srcKeyNode != null) {
                config.setSrcKey(srcKeyNode.getNodeValue());
            }
            if (targetNode != null) {
                config.setTargetKey(targetKeyNode.getNodeValue());
            }
            if (reversableNode != null) {
                config.setReversable(Boolean.valueOf(reversableNode.getNodeValue()));
            }
            if (batchNode != null) {
                config.setBatch(Boolean.valueOf(batchNode.getNodeValue()));
            }

            // 解析bean fields
            List<BeanMappingField> beanFields = parseMappingField(node);
            config.setBeanFields(beanFields);
            // 添加到返回结果
            mappings.add(config);
        }

        return mappings;
    }

    // 解析一下field-mapping
    private static List<BeanMappingField> parseMappingField(Node beanNode) {
        NodeList nodeList = beanNode.getChildNodes();
        List<BeanMappingField> beanFields = new ArrayList<BeanMappingField>(10);
        for (int i = 0; i < nodeList.getLength(); i++) {
            BeanMappingField beanField = new BeanMappingField();
            Node node = nodeList.item(i);
            if ("field-mapping".equals(node.getNodeName())) {
                Node srcNameNode = node.getAttributes().getNamedItem("srcName");
                Node srcClassNode = node.getAttributes().getNamedItem("srcClass");
                Node targetNameNode = node.getAttributes().getNamedItem("targetName");
                Node targetClassNode = node.getAttributes().getNamedItem("targetClass");
                Node defaultValueNode = node.getAttributes().getNamedItem("defaultValue");
                Node convetorNode = node.getAttributes().getNamedItem("convetor");
                Node scriptNode = node.getAttributes().getNamedItem("script");
                if (scriptNode == null && srcNameNode == null) {
                    throw new BeanMappingException("srcName or script is requied");
                }
                if (targetNameNode == null) {
                    throw new BeanMappingException("targetName is requied");
                }

                if (srcNameNode != null) {
                    beanField.setSrcName(srcNameNode.getNodeValue());
                }
                if (srcClassNode != null) {
                    beanField.setSrcClass(ReflectionHelper.forName(srcClassNode.getNodeValue()));
                }
                if (targetNameNode != null) {
                    beanField.setTargetName(targetNameNode.getNodeValue());
                }
                if (targetClassNode != null) {
                    beanField.setTargetClass(ReflectionHelper.forName(targetClassNode.getNodeValue()));
                }
                if (defaultValueNode != null) {
                    beanField.setDefaultValue(defaultValueNode.getNodeValue());
                }
                if (convetorNode != null) {
                    beanField.setConvertor(convetorNode.getNodeValue());
                }
                if (scriptNode != null) {
                    beanField.setScript(scriptNode.getNodeValue());
                }
                // 处理下mapping
                Node mappingNode = node.getAttributes().getNamedItem("mapping");
                if (mappingNode != null) {
                    beanField.setMapping(Boolean.valueOf(mappingNode.getNodeValue()));
                }

                beanFields.add(beanField);
            }
        }
        return beanFields;

    }

}
