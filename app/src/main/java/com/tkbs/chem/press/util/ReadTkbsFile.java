package com.tkbs.chem.press.util;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;


public class ReadTkbsFile {
    /**
     * byte数组中取int数值，本方法适用于(低位在前，高位在后)的顺序，和和intToBytes（）配套使用
     *
     * @param src    byte数组
     * @param offset 从数组的第offset位开始
     * @return int数值
     */
    public static int bytesToInt(byte[] src, int offset) {
        int value;
        value = (int) ((src[offset] & 0xFF) | ((src[offset + 1] & 0xFF) << 8)
                | ((src[offset + 2] & 0xFF) << 16) | ((src[offset + 3] & 0xFF) << 24));
        return value;
    }

    /**
     * byte数组中取int数值，本方法适用于(低位在后，高位在前)的顺序。和intToBytes2（）配套使用
     */
    public static int bytesToInt2(byte[] src, int offset) {
        int value;
        value = (int) (((src[offset] & 0xFF) << 24)
                | ((src[offset + 1] & 0xFF) << 16)
                | ((src[offset + 2] & 0xFF) << 8) | (src[offset + 3] & 0xFF));
        return value;
    }

    public static Object getObject(byte[] bytes) throws IOException, ClassNotFoundException {
        ByteArrayInputStream bi = new ByteArrayInputStream(bytes);
        ObjectInputStream oi = new ObjectInputStream(bi);
        Object obj = oi.readObject();
        bi.close();
        oi.close();
        return obj;
    }

    /**
     * file 转换  byte[]
     *
     * @param filePath
     * @return
     */
    public static byte[] file2Byte(String filePath) {
        byte[] buffer = null;
        try {
            File file = new File(filePath);
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return buffer;
    }

    /**
     * 从指定的字节开始读取：
     *
     * @param filePath 文件路径
     */
    public static int getIndexSize(String filePath, int reversoffset) {
        File tkbs = new File(filePath);
        long pos = tkbs.length() - reversoffset;
        byte[] b = new byte[4];
        int length = 0;
        try {
            RandomAccessFile randomAccessFile = new RandomAccessFile(filePath, "r");
            randomAccessFile.seek(pos);
            randomAccessFile.read(b);
            length = bytesToInt(b, 0);  //高位在后 低位在前
//			int length3 = (b[3] << 24) + (b[2] << 16) + (b[1] << 8) + b[0] ;
            randomAccessFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return length;
    }

    /**
     * 获取从指定的字节开始读取:
     *
     * @param filePath
     * @param reversoffset
     * @return
     */
    public static int getIndexSizeEx(String filePath, int reversoffset) {

        File tkbs = new File(filePath);
        long pos = tkbs.length() - reversoffset;
        FileInputStream filestream = null;
        DataInputStream dataStream = null;
        long length = 0;
        try {

            filestream = new FileInputStream(filePath);
            dataStream = new DataInputStream(filestream);
            dataStream.skip(pos);
            length = dataStream.readInt();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                dataStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                filestream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return (int) length;
    }

    /**
     * 对文件进行解密生成索引文件
     *
     * @param filePath  要解析的文件:
     * @param indexFile 要生成的索引文件:
     */

    public static void decodeTkbsFileIndex(String filePath, String indexFile) {
        try {
            File tkbs = new File(filePath);
            int encodeindexlen = getIndexSize(filePath, 4); //实际长度
            int decodeindexlen = getIndexSize(filePath, 8); //对应长度
            int encodeindexstartpos = (int) ((tkbs.length() - 8) - encodeindexlen);

            FileInputStream inputStream = null;
            byte[] data = new byte[encodeindexlen];

            inputStream = new FileInputStream(filePath);
            inputStream.skip(encodeindexstartpos); //指定开始位置:
            inputStream.read(data, 0, encodeindexlen);
            inputStream.close();

            //解密文件生成新的文件:
            InputStream inputStr = new ByteArrayInputStream(data);
            InputStream tkbStream = DESUtil.doDecryptTkbsFile(inputStr, Config.FILE_KEY);

            OutputStream out2 = new FileOutputStream(new File(indexFile));

            //将索引写入磁盘文件
            byte[] buffer2 = new byte[encodeindexlen];
            int r2 = 0;
            while ((r2 = tkbStream.read(buffer2)) > 0) {
                out2.write(buffer2, 0, r2);
            }
            tkbStream.close();
            inputStr.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void decodeIndex(String filePath, String indexFile, String encryptionKey) {
        File tkbs = null;
        byte[] outdata = null;
        FileInputStream inputStream = null;
        byte[] data = null;
        try {
            tkbs = new File(filePath);
            int encodeindexlen = getIndexSizeEx(filePath, 4); //实际长度
            int decodeindexlen = getIndexSizeEx(filePath, 8); //对应长度
            int encodeindexstartpos = (int) ((tkbs.length() - 8) - encodeindexlen);

            data = new byte[encodeindexlen];
            inputStream = new FileInputStream(filePath);
            inputStream.skip(encodeindexstartpos); //指定开始位置:
            inputStream.read(data, 0, encodeindexlen);
            inputStream.close();
            outdata = DESUtil.doDecryptFile(data, encryptionKey);
            OutputStream out = new FileOutputStream(new File(indexFile));
            out.write(outdata, 0, decodeindexlen);
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取管道中的流数据
     */
    public static byte[] readStream(InputStream inStream) {
        ByteArrayOutputStream bops = new ByteArrayOutputStream();
        int data = -1;
        try {
            while ((data = inStream.read()) != -1) {
                bops.write(data);
            }
            return bops.toByteArray();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 修改重置tkbs文件:
     */
    /**
     * 从指定的字符串开始读： 使用：RandomAccessFile
     */
    public static void editIndexFile(String filePath, String indexFile) {
        File tkbs = new File(filePath);
        String str = "";
        BufferedWriter writer = null;
        RandomAccessFile randomAccessFile = null;
        try {
            writer = new BufferedWriter(new FileWriter(indexFile));
            randomAccessFile = new RandomAccessFile(filePath, "r");
            while ((str = randomAccessFile.readLine()) != null) {
                int a = str.indexOf("</TK");
                if (a != -1) {
                    String newIndexEnd = "</TKBSFileIndex>";
                    str = newIndexEnd;
                }
                writer.append(str);
                writer.newLine();
                writer.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (randomAccessFile != null) {
                    randomAccessFile.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 测试：main
     *
     * @param args
     */
    public static void main(String[] args) {
        String basePath = "E:\\TKBS\\SampleBookPlatform\\DATA\\DocumentDBLocal\\" + "resource" + File.separator + "40DDF08E26FF4AB3B5D07A91D02E6288" + File.separator + "20180313151251751441.tkbs";
        String indexFile = "E:\\TKBS\\SampleBookPlatform\\DATA\\DocumentDBLocal\\" + "resource" + File.separator + "40DDF08E26FF4AB3B5D07A91D02E6288" + File.separator + "temp.xml";
    /*	String tkbFile = "E:/Test/complete.tkbs";
		String indexFile = "E:/Test/index.xml";
		String tempFile = "E:/Test/temp.txt";*/ //68-F7-28-C5-A4-35
//		String md5Mc= DESUtil.Md5("00-50-56-AA-00-86").substring(0,8);
//		String md5Mc= DESUtil.Md5("C4-8E-8F-BB-27-83").substring(0,8);
        String md5Mc = DESUtil.Md5("34-02-86-A5-18-69").substring(0, 8);
//		String md5Mc= "12345678";
        decodeIndex(basePath, indexFile, "12345678");

//		DESUtil.decryptFile(indexFile, "E:/Test/tkbs/", "temp.xml", "1234567890", false);

    }
}
