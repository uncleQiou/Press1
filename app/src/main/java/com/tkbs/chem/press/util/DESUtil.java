package com.tkbs.chem.press.util;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import sun.misc.BASE64Encoder;

public class DESUtil {

    private static final String KEY_ALGORITHM = "DES";

    private static final String CIPHER_ALGORITHM = "DES/ECB/PKCS5Padding";

    private static final String CIPHER_ALGORITHM_TKBSFILE = "DES/ECB/NoPadding";

    public static void main(String[] args) throws Exception {
        String androidAsk = "androidASK";
        String key = "1234567890";
        String toc = "http://192.168.1.120:8080/webFile\\toc.xml.html";
        InputStream is = null;
        //DESUtil.encryptUrlFile(toc, key,"123456", false);
        int n = 0;
        byte data[] = new byte[1000];
        OutputStream outStream = new FileOutputStream("E:\\index.xml");
        while ((n = is.read(data)) > 0) {
            outStream.write(data, 0, n);
        }
    }

    /**
     * 加密文件 输出加密文件
     */
    public static void encryptFile(String inputFilePath, String outDirPath, String outFileName, String mac_str, String accreditinfo) {

        String password = macToKey(mac_str);

        File inputFile = new File(inputFilePath);

        File outDir = new File(outDirPath);

        if (!inputFile.exists() || !inputFile.isFile()) {
            System.out.println("目标文件未找到,请确认路径是否正确：" + inputFilePath);
            return;
        }

        if (!outDir.exists()) {
            outDir.mkdirs();
        }

        if (outFileName == null || "".equals(outFileName)) {
            outFileName = inputFile.getName();
        }

        File outFile = new File(outDir, outFileName);

        try {
            if (accreditinfo != null && !accreditinfo.equals("")) {
                doEncryptFile(inputFile, outFile, password, accreditinfo);
            } else {
                doEncryptFile(inputFile, outFile, password);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 加密输出网络文件
     *
     * @param inputFilePath
     * @param
     * @param outFileName
     * @param mac_str
     * @param accreditinfo
     */
    public static void encryptUrlFile(String inputFilePath, String outFileName, String mac_str, String accreditinfo) throws Exception {

        String password = macToKey(mac_str);

        URL inputurl = new URL(inputFilePath);
        HttpURLConnection intpuCconn = (HttpURLConnection) inputurl.openConnection();
        InputStream is = new DataInputStream(intpuCconn.getInputStream());
        URL outurl = new URL(outFileName);
        URLConnection outconnection = outurl.openConnection();
        outconnection.setDoOutput(true);


        try {
            OutputStream out = null;
            CipherInputStream cis = null;
            try {
                SecureRandom random = new SecureRandom();
                SecretKey securekey = getSecretKeyByStr(password);
                // Cipher对象实际完成加密操作
                Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
                // 用密匙初始化Cipher对象
                cipher.init(Cipher.ENCRYPT_MODE, securekey, random);
                // 现在，获取数据并加密
                // 正式执行加密操作
                out = new DataOutputStream(outconnection.getOutputStream());
                cis = new CipherInputStream(is, cipher);
                byte[] buffer = new byte[1024];
                int r;
                while ((r = cis.read(buffer)) > 0) {
                    out.write(buffer, 0, r);
                }

            } finally {
                if (out != null) {
                    out.close();
                }
                if (cis != null) {
                    cis.close();
                }
                if (is != null) {
                    is.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void doEncryptFile(File inputFile, File outFile, String password) throws Exception {
        InputStream is = null;
        OutputStream out = null;
        CipherInputStream cis = null;
        try {
            SecureRandom random = new SecureRandom();
            SecretKey securekey = getSecretKeyByStr(password);
            // Cipher对象实际完成加密操作
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            // 用密匙初始化Cipher对象
            cipher.init(Cipher.ENCRYPT_MODE, securekey, random);
            // 现在，获取数据并加密
            // 正式执行加密操作
            is = new FileInputStream(inputFile);
            out = new FileOutputStream(outFile);
            cis = new CipherInputStream(is, cipher);
            byte[] buffer = new byte[1024];
            int r;
            while ((r = cis.read(buffer)) > 0) {
                out.write(buffer, 0, r);
            }

        } finally {
            if (out != null) {
                out.close();
            }
            if (cis != null) {
                cis.close();
            }
            if (is != null) {
                is.close();
            }
        }
    }

    private static void doEncryptFile(File inputFile, File outFile, String password, String accreditinfo) throws Exception {
        InputStream is = null;
        OutputStream out = null;
        CipherOutputStream cos = null;
        try {
            SecureRandom random = new SecureRandom();
            SecretKey securekey = getSecretKeyByStr(password);
            // Cipher对象实际完成加密操作
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            // 用密匙初始化Cipher对象
            cipher.init(Cipher.ENCRYPT_MODE, securekey, random);
            // 现在，获取数据并加密
            // 正式执行加密操作
            byte[] accreditinfoByte = getAccreditinfoByte(accreditinfo);

            is = new FileInputStream(inputFile);
            out = new FileOutputStream(outFile);
            cos = new CipherOutputStream(out, cipher);

            cos.write(accreditinfoByte);

            byte[] buffer = new byte[1024];
            int r;
            while ((r = is.read(buffer)) > 0) {
                cos.write(buffer, 0, r);
            }

        } finally {
            if (cos != null) {
                cos.close();
            }
            if (out != null) {
                out.close();
            }
            if (is != null) {
                is.close();
            }
        }
    }

    /**
     * 解密文件 输出解密文件
     *
     * @param inputFilePath
     * @param outDirPath
     * @param mac_str
     * @param hasaccredit
     */
    public static void decryptFile(String inputFilePath, String outDirPath, String outFileName, String mac_str, boolean hasaccredit) {

        String password = macToKey(mac_str);

        File inputFile = new File(inputFilePath);

        File outDir = new File(outDirPath);

        if (!inputFile.exists() || !inputFile.isFile()) {
            System.out.println("目标文件未找到,请确认路径是否正确：" + inputFilePath);
            return;
        }

        if (!outDir.exists()) {
            outDir.mkdirs();
        }

        if (outFileName == null || "".equals(outFileName)) {
            outFileName = inputFile.getName();
        }

        File outFile = new File(outDir, outFileName);

        try {
            if (hasaccredit) {
                doDecryptFile(inputFile, outFile, password, hasaccredit);
            } else {
                doDecryptFile(inputFile, outFile, password);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void doDecryptFile(File inputFile, File outFile, String password) throws Exception {
        InputStream is = null;
        OutputStream out = null;
        CipherOutputStream cos = null;
        try {
            // DES算法要求有一个可信任的随机数
            SecureRandom random = new SecureRandom();
            // 调用方法获取密钥
            SecretKey securekey = getSecretKeyByStr(password);
            // Cipher对象实际完成解密操作
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            // 用密匙初始化Cipher对象
            cipher.init(Cipher.DECRYPT_MODE, securekey, random);
            // 真正des解密操作

            // /System.out.println("解密前的大小：" + b_file.length());
            is = new FileInputStream(inputFile);
            out = new FileOutputStream(outFile);
            cos = new CipherOutputStream(out, cipher);

            // 解密后的文件写入磁盘
            byte[] buffer = new byte[1024];
            int r = 0;
            while ((r = is.read(buffer)) > 0) {
                cos.write(buffer, 0, r);
            }

        } finally {
            if (cos != null) {
                cos.close();
            }
            if (out != null) {
                out.close();
            }
            if (is != null) {
                is.close();
            }
        }
    }

    private static void doDecryptFile(File inputFile, File outFile, String password, boolean flag) throws Exception {
        InputStream is = null;
        OutputStream out = null;
        CipherInputStream cis = null;
        try {
            // DES算法要求有一个可信任的随机数
            SecureRandom random = new SecureRandom();
            // 调用方法获取密钥
            SecretKey securekey = getSecretKeyByStr(password);
            // Cipher对象实际完成解密操作
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            // 用密匙初始化Cipher对象
            cipher.init(Cipher.DECRYPT_MODE, securekey, random);
            // 真正des解密操作

            // /System.out.println("解密前的大小：" + b_file.length());
            is = new FileInputStream(inputFile);
            out = new FileOutputStream(outFile);
            cis = new CipherInputStream(is, cipher);

            // 获取授权信息
            String accreditinfo = doGetAccreditinfo(cis);

            // 验证授权信息
            boolean bol = auditAccreditinfo(accreditinfo);
            if (bol) {
                // 解密后的文件写入磁盘
                byte[] buffer = new byte[1024];
                int r = 0;
                while ((r = cis.read(buffer)) > 0) {
                    out.write(buffer, 0, r);
                }
            } else {
                // 未验证通过
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                out.close();
            }
            if (cis != null) {
                cis.close();
            }
            if (is != null) {
                is.close();
            }
        }
    }

    /**
     * 解密文件 输出 输入流
     */
    public static InputStream decryptFile(String inputFilePath, String mac_str, boolean hasaccredit) {
        InputStream returnIs = null;

        String password = macToKey(mac_str);

        File inputFile = new File(inputFilePath);

        if (!inputFile.exists() || !inputFile.isFile()) {
            System.out.println("目标文件未找到,请确认路径是否正确：" + inputFilePath);
            return null;
        }
        try {
            InputStream is = new FileInputStream(inputFile);
            if (hasaccredit) {
                returnIs = doDecryptFile(is, password, hasaccredit);
            } else {
                returnIs = doDecryptFile(is, password);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnIs;
    }

    /**
     * 解密文件 输出 输入流
     *
     * @param inputFilePath
     * @param
     * @param mac_str
     * @param hasaccredit
     */
    public static InputStream decryptSmbFile(String inputFilePath, String mac_str, boolean hasaccredit) throws Exception {
        InputStream returnIs = null;
        URL url = new URL(inputFilePath);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        try {
            String password = macToKey(mac_str);
            try {
                InputStream is = new DataInputStream(conn.getInputStream());
                if (hasaccredit) {
                    returnIs = doDecryptFile(is, password, hasaccredit);
                } else {
                    returnIs = doDecryptFile(is, password);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnIs;
    }

    public static InputStream doDecryptFile(InputStream is, String password) throws Exception {
        CipherInputStream cis = null;

        // DES算法要求有一个可信任的随机数
        SecureRandom random = new SecureRandom();
        // 调用方法获取密钥
        SecretKey securekey = getSecretKeyByStr(password);
        // Cipher对象实际完成解密操作
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        // 用密匙初始化Cipher对象
        cipher.init(Cipher.DECRYPT_MODE, securekey, random);
        // 真正des解密操作

        cis = new CipherInputStream(is, cipher);

        return cis;
    }

    private static InputStream doDecryptFile(InputStream is, String password, boolean hasaccredit) throws Exception {

        CipherInputStream cis = null;

        // DES算法要求有一个可信任的随机数
        SecureRandom random = new SecureRandom();
        // 调用方法获取密钥
        SecretKey securekey = getSecretKeyByStr(password);
        // Cipher对象实际完成解密操作
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        // 用密匙初始化Cipher对象
        cipher.init(Cipher.DECRYPT_MODE, securekey, random);
        // 真正des解密操作

        cis = new CipherInputStream(is, cipher);

        // 获取授权信息
        String accreditinfo = doGetAccreditinfo(cis);

        // 验证授权信息
        boolean bol = auditAccreditinfo(accreditinfo);
        if (bol) {
            // 验证通过
            return cis;
        } else {
            // 未验证通过
            return null;
        }

    }

    /**
     * 解密文件 输出 输入流（小文件可使用本方法）
     * decryptFile返回图片流失页面无法显示
     * decryptMemoryFile将文件现在解密至内存中再返回到页面
     *
     * @param inputFilePath
     * @param mac_str
     * @param hasaccredit
     * @return
     */
    public static InputStream decryptFileByMemory(String inputFilePath, String mac_str, boolean hasaccredit) {
        InputStream returnIs = null;

        String password = macToKey(mac_str);

        File inputFile = new File(inputFilePath);

        if (!inputFile.exists() || !inputFile.isFile()) {
            System.out.println("目标文件未找到,请确认路径是否正确：" + inputFilePath);
            return null;
        }
        try {
            if (hasaccredit) {
                returnIs = doDecryptFile(inputFile, password, hasaccredit);
            } else {
                returnIs = doDecryptFile(inputFile, password);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnIs;
    }

    public static InputStream decryptFileByMemoryUrl(String inputFilePath, String mac_str, boolean hasaccredit) {
        InputStream returnIs = null;
        String password = macToKey(mac_str);

        try {
            URL inputurl = new URL(inputFilePath);
            HttpURLConnection intpuCconn = (HttpURLConnection) inputurl.openConnection();
            InputStream is = new DataInputStream(intpuCconn.getInputStream());
            returnIs = doDecryptUrlFile(inputFilePath, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnIs;
    }

    public static InputStream doDecryptFile(File inputFile, String password) throws Exception {
        InputStream is = null;
        ByteArrayInputStream bis = null;
        ByteArrayOutputStream out = null;
        CipherOutputStream cos = null;
        try {
            // DES算法要求有一个可信任的随机数
            SecureRandom random = new SecureRandom();
            // 调用方法获取密钥
            SecretKey securekey = getSecretKeyByStr(password);
            // Cipher对象实际完成解密操作
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            // 用密匙初始化Cipher对象
            cipher.init(Cipher.DECRYPT_MODE, securekey, random);
            // 真正des解密操作

            is = new FileInputStream(inputFile);
            out = new ByteArrayOutputStream();
            cos = new CipherOutputStream(out, cipher);

            // 解密后的文件写入内存
            byte[] buffer = new byte[1024];
            int r = 0;
            while ((r = is.read(buffer)) > 0) {
                cos.write(buffer, 0, r);
            }
            cos.close();
            out.close();
            is.close();
            bis = new ByteArrayInputStream(out.toByteArray());
        } finally {
            if (cos != null) {
                cos.close();
            }
            if (out != null) {
                out.close();
            }
            if (is != null) {
                is.close();
            }
        }
        return bis;
    }

    private static InputStream doDecryptUrlFile(String inputFile, String password) throws Exception {
        InputStream is = null;
        ByteArrayInputStream bis = null;
        ByteArrayOutputStream out = null;
        CipherOutputStream cos = null;
        try {
            // DES算法要求有一个可信任的随机数
            SecureRandom random = new SecureRandom();
            // 调用方法获取密钥
            SecretKey securekey = getSecretKeyByStr(password);
            // Cipher对象实际完成解密操作
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            // 用密匙初始化Cipher对象
            cipher.init(Cipher.DECRYPT_MODE, securekey, random);
            // 真正des解密操作

            URL inputurl = new URL(inputFile);
            HttpURLConnection intpuCconn = (HttpURLConnection) inputurl.openConnection();
            is = new DataInputStream(intpuCconn.getInputStream());
            out = new ByteArrayOutputStream();
            cos = new CipherOutputStream(out, cipher);

            // 解密后的文件写入内存
            byte[] buffer = new byte[1024];
            int r = 0;
            while ((r = is.read(buffer)) > 0) {
                cos.write(buffer, 0, r);
            }
            cos.close();
            out.close();
            is.close();
            bis = new ByteArrayInputStream(out.toByteArray());
        } finally {
            if (cos != null) {
                cos.close();
            }
            if (out != null) {
                out.close();
            }
            if (is != null) {
                is.close();
            }
        }
        return bis;
    }

    private static InputStream doDecryptFile(File inputFile, String password, boolean hasaccredit) throws Exception {

        InputStream is = null;
        ByteArrayInputStream bis = null;
        ByteArrayOutputStream bos = null;
        CipherInputStream cis = null;
        try {
            // DES算法要求有一个可信任的随机数
            SecureRandom random = new SecureRandom();
            // 调用方法获取密钥
            SecretKey securekey = getSecretKeyByStr(password);
            // Cipher对象实际完成解密操作
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            // 用密匙初始化Cipher对象
            cipher.init(Cipher.DECRYPT_MODE, securekey, random);
            // 真正des解密操作

            is = new FileInputStream(inputFile);
            cis = new CipherInputStream(is, cipher);

            // 获取授权信息
            String accreditinfo = doGetAccreditinfo(cis);

            // 验证授权信息
            boolean bol = auditAccreditinfo(accreditinfo);
            if (bol) {
                // 验证通过
                // 解密后的文件写入内存
                bos = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int r = 0;
                while ((r = cis.read(buffer)) > 0) {
                    bos.write(buffer, 0, r);
                }
                bos.close();
                cis.close();
                is.close();
                bis = new ByteArrayInputStream(bos.toByteArray());
            } else {
                // 未验证通过
            }
        } finally {
            if (bos != null) {
                bos.close();
            }
            if (cis != null) {
                cis.close();
            }
            if (is != null) {
                is.close();
            }
        }
        return bis;
    }

    /**
     * 解密文件流 输出文件
     */
    public static void decryptInputStream(InputStream is, String outDirPath, String outFileName, String mac_str, boolean hasaccredit) {

        String password = macToKey(mac_str);

        File outDir = new File(outDirPath);

        if (is == null) {
            System.out.println("输入流为空");
            return;
        }

        if (!outDir.exists()) {
            outDir.mkdirs();
        }

        if (outFileName == null || "".equals(outFileName)) {
            System.out.println("输出文件名称为空");
            return;
        }

        File outFile = new File(outDir, outFileName);

        try {
            if (hasaccredit) {
                doDecryptInputStream(is, outFile, password, hasaccredit);
            } else {
                doDecryptInputStream(is, outFile, password);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void doDecryptInputStream(InputStream is, File outFile, String password) throws Exception {
        OutputStream out = null;
        CipherOutputStream cos = null;
        try {
            // DES算法要求有一个可信任的随机数
            SecureRandom random = new SecureRandom();
            // 调用方法获取密钥
            SecretKey securekey = getSecretKeyByStr(password);
            // Cipher对象实际完成解密操作
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            // 用密匙初始化Cipher对象
            cipher.init(Cipher.DECRYPT_MODE, securekey, random);
            // 真正des解密操作

            // /System.out.println("解密前的大小：" + b_file.length());
            out = new FileOutputStream(outFile);
            cos = new CipherOutputStream(out, cipher);

            // 解密后的文件写入磁盘
            byte[] buffer = new byte[1024];
            int r = 0;
            while ((r = is.read(buffer)) > 0) {
                cos.write(buffer, 0, r);
            }

        } finally {
            if (cos != null) {
                cos.close();
            }
            if (out != null) {
                out.close();
            }
            if (is != null) {
                is.close();
            }
        }
    }

    private static void doDecryptInputStream(InputStream is, File outFile, String password, boolean flag) throws Exception {
        OutputStream out = null;
        CipherInputStream cis = null;
        try {
            // DES算法要求有一个可信任的随机数
            SecureRandom random = new SecureRandom();
            // 调用方法获取密钥
            SecretKey securekey = getSecretKeyByStr(password);
            // Cipher对象实际完成解密操作
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            // 用密匙初始化Cipher对象
            cipher.init(Cipher.DECRYPT_MODE, securekey, random);
            // 真正des解密操作

            // /System.out.println("解密前的大小：" + b_file.length());
            out = new FileOutputStream(outFile);
            cis = new CipherInputStream(is, cipher);

            // 获取授权信息
            String accreditinfo = doGetAccreditinfo(cis);

            // 验证授权信息
            boolean bol = auditAccreditinfo(accreditinfo);
            if (bol) {
                // 解密后的文件写入磁盘
                byte[] buffer = new byte[1024];
                int r = 0;
                while ((r = cis.read(buffer)) > 0) {
                    out.write(buffer, 0, r);
                }
            } else {
                // 未验证通过
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                out.close();
            }
            if (cis != null) {
                cis.close();
            }
            if (is != null) {
                is.close();
            }
        }
    }

    /**
     * 将授权信息转换为byte数组
     *
     * @param accreditinfo
     * @return
     */
    private static byte[] getAccreditinfoByte(String accreditinfo) {

        // 将授权信息转化为byte数组
        byte[] bstr = accreditinfo.getBytes();

        // 获取授权信息的长度 -- 生成000001 类型的6位字符串
        int blength = bstr.length;
        String blengthstr = blength + "";
        int strlength = blengthstr.length();
        String head_length = "";
        for (int i = 0; i < 6 - strlength; i++) {
            head_length += "0";
        }
        head_length += blengthstr;

        // 将长度信息转化为byte数组
        byte[] bstrlength = head_length.getBytes();

        byte[] bytes = new byte[bstrlength.length + bstr.length];
        System.arraycopy(bstrlength, 0, bytes, 0, bstrlength.length);
        System.arraycopy(bstr, 0, bytes, bstrlength.length, bstr.length);
        return bytes;

    }

    /**
     * 获取文件授权信息
     *
     * @param inputFilePath
     * @param mac_str
     * @return
     */
    public static String getAccreditinfo(String inputFilePath, String mac_str) {
        String accreditinfo = "";

        String password = macToKey(mac_str);

        File inputFile = new File(inputFilePath);

        if (!inputFile.exists() || !inputFile.isFile()) {
            System.out.println("目标文件未找到,请确认路径是否正确：" + inputFilePath);
            return accreditinfo;
        }

        try {
            accreditinfo = doGetAccreditinfo(inputFile, password);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return accreditinfo;
    }

    private static String doGetAccreditinfo(File inputFile, String password) throws Exception {
        String accreditinfo = "";
        InputStream is = null;
        CipherInputStream cis = null;
        try {
            // DES算法要求有一个可信任的随机数
            SecureRandom random = new SecureRandom();
            // 调用方法获取密钥
            SecretKey securekey = getSecretKeyByStr(password);
            // Cipher对象实际完成解密操作
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            // 用密匙初始化Cipher对象
            cipher.init(Cipher.DECRYPT_MODE, securekey, random);
            // 真正des解密操作

            is = new FileInputStream(inputFile);
            cis = new CipherInputStream(is, cipher);

            // 获取授权信息
            accreditinfo = doGetAccreditinfo(cis);

        } finally {
            if (cis != null) {
                cis.close();
            }
            if (is != null) {
                is.close();
            }
        }

        return accreditinfo;

    }

    private static String doGetAccreditinfo(CipherInputStream cis) throws Exception {
        String accredinfo = "";
        // 头信息长度字节数组
        byte[] head_lengthbs = new byte[6];
        cis.read(head_lengthbs);
        // 获取授权信息
        String head_lengthstr = new String(head_lengthbs);
        // 授权文件的前段长度
        int head_length = Integer.parseInt(head_lengthstr);
        // 授权信息的字节数组
        byte[] info_bs = new byte[head_length];
        cis.read(info_bs);
        // 授权信息
        accredinfo = new String(info_bs);

        return accredinfo;
    }

    /**
     * 获得输出的加密流（文件操作时（图片压缩）时直接加密文件（图片)）
     *
     * @param outpath
     * @param mac_str
     * @return
     * @throws Exception
     */
    public static OutputStream desCrypto(String outpath, String mac_str) throws Exception {
        String password = macToKey(mac_str);

        CipherOutputStream cos = null;
        OutputStream out = null;
        try {
            SecureRandom random = new SecureRandom();
            SecretKey securekey = getSecretKeyByStr(password);
            // Cipher对象实际完成加密操作
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            // 用密匙初始化Cipher对象
            cipher.init(Cipher.ENCRYPT_MODE, securekey, random);
            out = new FileOutputStream(new File(outpath));
            // 正式执行加密操作
            cos = new CipherOutputStream(out, cipher);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return cos;
    }

    /**
     * 验证授权信息
     *
     * @param accreditinfo
     * @return
     */
    public static boolean auditAccreditinfo(String accreditinfo) {

        return true;
    }

    /**
     * 将字符串转化成des的密钥
     *
     * @param password
     * @return
     */
    private static SecretKey getSecretKeyByStr(String password) {
        // 将DESKeySpec对象转换成SecretKey对象
        SecretKey securekey = null;
        try {
            // 创建DESKeySpec对象
            DESKeySpec desKey = new DESKeySpec(password.getBytes());
            // 创建密钥工厂
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(KEY_ALGORITHM);
            // 把DESKeySpec转换成SecretKey对象
            securekey = keyFactory.generateSecret(desKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return securekey;
    }

    /**
     * 将MAC加密再截取8位
     *
     * @param mac
     * @return
     */
    private static String macToKey(String mac) {
        String mac_md5 = Md5(mac).substring(8, 17);
        return mac_md5;
    }

    /**
     * MD5加密
     *
     * @param plainText
     * @return
     */
    public static String Md5(String plainText) {
        String result = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(plainText.getBytes());
            byte b[] = md.digest();
            int i;
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0) {
                    i += 256;
                }
                if (i < 16) {
                    buf.append("0");
                }
                buf.append(Integer.toHexString(i));
            }
            result = buf.toString();
            // System.out.println("result: " + buf.toString());//32位的加密
            // System.out.println("result: " +
            // buf.toString().substring(8,24));//16位的加密
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * Description 根据键值进行加密
     *
     * @param data
     * @param key  加密键byte数组
     * @return
     * @throws Exception
     */
    public static String encrypt(String data, String key) throws Exception {
        byte[] bt = encrypt(data.getBytes(), key.getBytes());
        String strs = new BASE64Encoder().encode(bt);
        // String strs = Base64Utils.encode(bt);
        return strs;
    }

    /**
     * Description 根据键值进行解密
     *
     * @param data
     * @param key  加密键byte数组
     * @return
     * @throws IOException
     * @throws Exception
     */
    public static String decrypt(String data, String key) throws IOException, Exception {
        if (data == null)
            return null;
   /*     byte[] buf = new BASE64Decoder().decodeBuffer(data);
        // byte[] buf = Base64Utils.decode(data.toCharArray());
        byte[] bt = decrypt(buf, key.getBytes());*/

        byte[] bt = encrypt(data.getBytes(), key.getBytes());
        String strs = new BASE64Encoder().encode(bt);
        /*return new String(bt);*/
        return strs;
    }

    /**
     * Description 根据键值进行加密
     *
     * @param data
     * @param key  加密键byte数组
     * @return
     * @throws Exception
     */
    private static byte[] encrypt(byte[] data, byte[] key) throws Exception {
        // 生成一个可信任的随机数源
        SecureRandom sr = new SecureRandom();

        // 从原始密钥数据创建DESKeySpec对象
        DESKeySpec dks = new DESKeySpec(key);

        // 创建一个密钥工厂，然后用它把DESKeySpec转换成SecretKey对象
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(KEY_ALGORITHM);
        SecretKey securekey = keyFactory.generateSecret(dks);

        // Cipher对象实际完成加密操作
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);

        // 用密钥初始化Cipher对象
        cipher.init(Cipher.ENCRYPT_MODE, securekey, sr);

        return cipher.doFinal(data);
    }

    /**
     * Description 根据键值进行解密
     *
     * @param data
     * @param key  加密键byte数组
     * @return
     * @throws Exception
     */
    private static byte[] decrypt(byte[] data, byte[] key) throws Exception {
        // 生成一个可信任的随机数源
        SecureRandom sr = new SecureRandom();

        // 从原始密钥数据创建DESKeySpec对象
        DESKeySpec dks = new DESKeySpec(key);

        // 创建一个密钥工厂，然后用它把DESKeySpec转换成SecretKey对象
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(KEY_ALGORITHM);
        SecretKey securekey = keyFactory.generateSecret(dks);

        // Cipher对象实际完成解密操作
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);

        // 用密钥初始化Cipher对象
        cipher.init(Cipher.DECRYPT_MODE, securekey, sr);

        return cipher.doFinal(data);
    }

    static class Base64Utils {

        static private char[] alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=".toCharArray();
        static private byte[] codes = new byte[256];

        static {
            for (int i = 0; i < 256; i++) {
                codes[i] = -1;
            }
            for (int i = 'A'; i <= 'Z'; i++) {
                codes[i] = (byte) (i - 'A');
            }
            for (int i = 'a'; i <= 'z'; i++) {
                codes[i] = (byte) (26 + i - 'a');
            }
            for (int i = '0'; i <= '9'; i++) {
                codes[i] = (byte) (52 + i - '0');
            }
            codes['+'] = 62;
            codes['/'] = 63;
        }

        /**
         * 将原始数据编码为base64编码
         */
        static public String encode(byte[] data) {
            char[] out = new char[((data.length + 2) / 3) * 4];
            for (int i = 0, index = 0; i < data.length; i += 3, index += 4) {
                boolean quad = false;
                boolean trip = false;
                int val = (0xFF & (int) data[i]);
                val <<= 8;
                if ((i + 1) < data.length) {
                    val |= (0xFF & (int) data[i + 1]);
                    trip = true;
                }
                val <<= 8;
                if ((i + 2) < data.length) {
                    val |= (0xFF & (int) data[i + 2]);
                    quad = true;
                }
                out[index + 3] = alphabet[(quad ? (val & 0x3F) : 64)];
                val >>= 6;
                out[index + 2] = alphabet[(trip ? (val & 0x3F) : 64)];
                val >>= 6;
                out[index + 1] = alphabet[val & 0x3F];
                val >>= 6;
                out[index + 0] = alphabet[val & 0x3F];
            }

            return new String(out);
        }

        /**
         * 将base64编码的数据解码成原始数据
         */
        static public byte[] decode(char[] data) {
            int len = ((data.length + 3) / 4) * 3;
            if (data.length > 0 && data[data.length - 1] == '=') {
                --len;
            }
            if (data.length > 1 && data[data.length - 2] == '=') {
                --len;
            }
            byte[] out = new byte[len];
            int shift = 0;
            int accum = 0;
            int index = 0;
            for (int ix = 0; ix < data.length; ix++) {
                int value = codes[data[ix] & 0xFF];
                if (value >= 0) {
                    accum <<= 6;
                    shift += 6;
                    accum |= value;
                    if (shift >= 8) {
                        shift -= 8;
                        out[index++] = (byte) ((accum >> shift) & 0xff);
                    }
                }
            }
            if (index != out.length) {
                throw new Error("miscalculated data length!");
            }
            return out;
        }
    }

    public static String getFileAccertInfo(String filepath,
                                           String mac_str, boolean hasaccredit) {
        InputStream returnIs = null;

        String password = macToKey(mac_str);

        File inputFile = new File(filepath);

        if (!inputFile.exists() || !inputFile.isFile()) {
            System.out.println("目标文件未找到,请确认路径是否正确：" + filepath);
            return null;
        }
        try {
            String accredinfo = "";
            InputStream is = new FileInputStream(inputFile);
            if (hasaccredit) {
                returnIs = doDecryptFile(is, password, hasaccredit);
            } else {
                returnIs = doDecryptFile(is, password);
            }
            // 头信息长度字节数组
            byte[] head_lengthbs = new byte[6];
            returnIs.read(head_lengthbs);
            // 获取授权信息
            String head_lengthstr = new String(head_lengthbs);
            // 授权文件的前段长度
            int head_length = Integer.parseInt(head_lengthstr);
            // 授权信息的字节数组
            byte[] info_bs = new byte[head_length];
            returnIs.read(info_bs);
            // 授权信息
            accredinfo = new String(info_bs);

            return accredinfo;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static InputStream doDecryptTkbsFile(InputStream is, String password) throws Exception {
        CipherInputStream cis = null;

        // DES算法要求有一个可信任的随机数
        SecureRandom random = new SecureRandom();
        // 调用方法获取密钥
        SecretKey securekey = getSecretKeyByStr(password);
        // Cipher对象实际完成解密操作
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM_TKBSFILE);
        // 用密匙初始化Cipher对象
        cipher.init(Cipher.DECRYPT_MODE, securekey, random);

        // 真正des解密操作
        cis = new CipherInputStream(is, cipher);

        return cis;
    }

    /**
     * 解密生成的tkbs文件:
     *
     * @author admin
     */
    public static byte[] doDecryptFile(byte[] src, String password) throws Exception {
        // DES算法要求有一个可信任的随机数
        SecureRandom random = new SecureRandom();
        // 调用方法获取密钥
        SecretKey securekey = getSecretKeyByStr(password);
        // Cipher对象实际完成解密操作
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM_TKBSFILE);
        // 用密匙初始化Cipher对象
        cipher.init(Cipher.DECRYPT_MODE, securekey, random);
        // 真正des解密操
        return cipher.doFinal(src);
    }


    public static byte[] encryptNoPadding(byte[] outdata, String password) throws Exception {
        // 生成一个可信任的随机数源
        // DES算法要求有一个可信任的随机数
        SecureRandom random = new SecureRandom();
        // 调用方法获取密钥
        SecretKey securekey = getSecretKeyByStr(password);
        // Cipher对象实际完成解密操作
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM_TKBSFILE);
        // 用密匙初始化Cipher对象
        cipher.init(Cipher.ENCRYPT_MODE, securekey, random);
        // 真正des解密操
        return cipher.doFinal(outdata);
    }

    /**
     * 用户登录密码加密
     *
     * @param s
     * @return 密码加密成功返回string字符串，失败返回null
     */
    public final static String MD5(String s) {
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        try {
            byte[] btInput = s.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 创建指定数量的随机字符串
     *
     * @param numberFlag 是否是数字
     * @param length
     * @return
     */
    public static String createRandom(boolean numberFlag, int length) {
        String retStr = "";
        String strTable = numberFlag ? "1234567890" : "1234567890abcdefghijkmnpqrstuvwxyz";
        int len = strTable.length();
        boolean bDone = true;
        do {
            retStr = "";
            int count = 0;
            for (int i = 0; i < length; i++) {
                double dblR = Math.random() * len;
                int intR = (int) Math.floor(dblR);
                char c = strTable.charAt(intR);
                if (('0' <= c) && (c <= '9')) {
                    count++;
                }
                retStr += strTable.charAt(intR);
            }
            if (count >= 2) {
                bDone = false;
            }
        } while (bDone);

        return retStr;
    }
}

