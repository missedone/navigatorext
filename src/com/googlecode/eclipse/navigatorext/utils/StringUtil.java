/*
 * Created on 2005-6-7
 *
 */
package com.googlecode.eclipse.navigatorext.utils;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.StringTokenizer;

public class StringUtil {

    static final char hexDigit[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

    /**
     * ת������
     * 
     * @param src ��Ҫ������ַ�
     * @param encoding ��Ҫת��Ϊ�ı���
     * @return
     */
    public static String convertEncoding(String src, String encoding) {
        try {
            return new String(src.getBytes(encoding));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("��֧��" + encoding + "����");
        }
    }

    /**
     * Encodes a String from unicode to the specified encoding. This method returns null if the encoding is not
     * supported
     * 
     * @param encoding
     * @param text
     * @return byte[]
     */
    public static byte[] encode(String encoding, String text) {
        if (text != null) {
            if (encoding == null)
                return text.getBytes();

            try {
                return text.getBytes(encoding);
            } catch (UnsupportedEncodingException e) {
            }
        }

        return null;
    }

    /**
     * Decodes a byte array from specified encoding to a unicode String. This method returns null if the encoding is not
     * supported
     * 
     * @param encoding
     * @param text
     * @return byte[]
     */
    public static String decode(String encoding, byte[] data) {
        if (data != null) {
            if (encoding == null)
                return new String(data);

            try {
                return new String(data, encoding);
            } catch (UnsupportedEncodingException e) {
            }
        }

        return null;
    }

    /**
     * ��ȥ�ַ�
     * 
     * @param src Դ�ַ�
     * @param ch Ҫȥ����Ŀ���ַ�
     * @param nLen Ŀ�곤�ȣ�<=0,ֱ��û��Ŀ���ַ�Ϊֹ��
     * @return
     */
    public static String ltrim(String src, char ch, int nLen) {
        if (src == null || src.length() <= nLen)
            return src;

        char[] czSrc = src.toCharArray();
        int i, j;
        int n = czSrc.length;
        for (i = 0; (n - i) > nLen; i++) {
            if (czSrc[i] != ch)
                break;
        }

        char[] czRet = new char[n - i];
        for (j = 0; i < czSrc.length; i++, j++)
            czRet[j] = czSrc[i];

        return new String(czRet);
    }

    /**
     * ��ȥ�ַ�
     * 
     * @param src Դ�ַ�
     * @param ch Ҫȥ����Ŀ���ַ�
     * @param nLen Ŀ�곤�ȣ�<=0,ֱ��û��Ŀ���ַ�Ϊֹ��
     * @return
     */
    public static String rtrim(String src, char ch, int nLen) {
        if (src == null || src.length() <= nLen)
            return src;

        char[] czSrc = src.toCharArray();
        int i, j;
        int n = czSrc.length;
        for (i = n - 1; i >= nLen; i--) {
            if (czSrc[i] != ch)
                break;
        }

        char[] czRet = new char[i + 1];
        for (j = i; i >= 0; i--, j--)
            czRet[j] = czSrc[i];

        return new String(czRet);
    }

    /**
     * �ж��Ƿ�Ϊ����
     * 
     * @param str �����ַ�, �ɰ�+,-, С���
     * @return
     */
    public static boolean isNumber(String str) {
        String test = str;
        if (test.startsWith("-") || test.startsWith("+"))
            test = test.substring(1);

        int index = test.indexOf('.');
        if (index < 0)
            return isDigit(test);
        else {
            return isDigit(test.substring(0, index)) && isDigit(test.substring(index + 1));
        }
    }

    /**
     * �ж��Ƿ�ȫ������
     * 
     * @param str
     * @return
     */
    public static boolean isDigit(String str) {
        int len = str.length();
        for (int i = 0; i < len; i++) {
            boolean ret = Character.isDigit(str.charAt(i));
            if (!ret)
                return false;
        }
        return true;
    }

    /**
     * �ж��Ƿ�ȫ���ַ�
     * 
     * @param str
     * @return
     */
    public static boolean isLetter(String str) {
        int len = str.length();
        for (int i = 0; i < len; i++) {
            boolean ret = Character.isLetter(str.charAt(i));
            if (!ret)
                return false;
        }
        return true;
    }

    /**
     * �ж����ַ������
     * 
     * @param str
     * @return
     */
    public static boolean isLetterOrDigit(String str) {
        int len = str.length();
        for (int i = 0; i < len; i++) {
            boolean ret = Character.isLetterOrDigit(str.charAt(i));
            if (!ret)
                return false;
        }
        return true;
    }

    /**
     * �÷�ͬsql��substr
     * 
     * @param src
     * @param nStart
     * @param nLen
     * @return
     */
    public static String substr(String src, int nStart, int nLen) {
        if (src == null)
            return null;

        byte[] bySrc = src.getBytes();
        byte[] byRet = new byte[nLen];
        int i, j;
        for (i = nStart, j = 0; i < bySrc.length && j < nLen; i++, j++)
            byRet[j] = bySrc[i];

        return new String(byRet, 0, j);
    }

    /**
     * @param src
     * @param nStart
     * @return
     */
    public static String substr(String src, int nStart) {
        return substr(src, nStart, src.getBytes().length);
    }

    /**
     * 
     * @param raw
     * @param args
     * @return
     */
    public static String format(String raw, String[] args) {
        StringBuffer buffer = new StringBuffer();
        int start = 0;
        int mark = raw.indexOf("{");
        if (mark < 0)
            return raw;
        while (mark >= 0) {
            buffer.append(raw.substring(start, mark));
            int markEnd = raw.indexOf('}', mark);
            start = markEnd + 1;

            String value = "";
            String sIndx = raw.substring(mark + 1, markEnd);
            if (isDigit(sIndx)) {
                int nIndx = Integer.parseInt(sIndx);
                if (nIndx >= 0 && nIndx < args.length)
                    value = args[nIndx];
            }
            buffer.append(value);

            mark = raw.indexOf("{", markEnd);

        }
        buffer.append(raw.substring(start));

        return buffer.toString();
    }

    public static boolean isNull(String str) {
        if (str == null || "".equals(str.trim()))
            return true;
        return false;
    }

    public static String[] getTokens(String sSource, String sDelim) {

        StringTokenizer tokenizer = new StringTokenizer(sSource, sDelim);
        int iCount = tokenizer.countTokens();
        String[] sTokens = new String[iCount];
        for (int i = 0; i < iCount; i++) {
            sTokens[i] = tokenizer.nextToken();
        }
        return (sTokens);
    }

    public static String fillChar(String sSource, char ch, int nLen, boolean bLeft) {
        int nSrcLen = sSource.length();

        if (nSrcLen <= nLen) { // �����
            StringBuffer buffer = new StringBuffer();
            if (bLeft) {
                for (int i = 0; i < (nLen - nSrcLen); i++) {
                    buffer.append(ch);
                }
                buffer.append(sSource);
            } else // �����
            {
                buffer.append(sSource);
                for (int i = 0; i < (nLen - nSrcLen); i++)
                    buffer.append(ch);
            }
            return (buffer.toString());
        }
        return sSource;
    }

    /**
     * ��ָ����ʽ���ָ���ַ��ַ���
     * 
     * @param sSource Ҫ�����ַ�
     * @param ch ����ַ�
     * @param nLen ���󳤶�
     * @param nLen ��䷽��true:��ߣ�false:�ұ�
     * @return �����ַ�
     * @since StringTool 1.0
     */
    public static String convertToHex(byte[] bySrc, int offset, int len) {
        byte[] byNew = new byte[len];
        String sTmp = "";
        String sResult = "";

        for (int i = 0; i < len; i++)
            byNew[i] = bySrc[offset + i];

        for (int i = 0, n = byNew.length; i < n; i++) {
            sTmp = Integer.toHexString(byNew[i] & 0XFF);
            sTmp = fillChar(sTmp, '0', 2, true) + " ";
            sResult += sTmp;
        }
        return (sResult);
    }

    public static String formatInHex(byte[] bySrc, int nLineLen) {
        final String sRepalce = "\n\t\r\0";
        int nLength = bySrc.length;

        int nLine = nLength / nLineLen + 1;

        String sLineTmp = "";
        byte[] byNew = new byte[nLine * nLineLen];
        for (int i = 0; i < nLength; i++)
            byNew[i] = bySrc[i];

        String sRet = "";

        for (int i = 0; i < nLine; i++) {
            if (byNew[(i + 1) * nLineLen - 1] < 0) {
                sLineTmp = new String(byNew, i * nLineLen, nLineLen - 1);
                sLineTmp += "?";
            } else {
                if (byNew[i * nLineLen] < 0) {
                    sLineTmp = new String(byNew, i * nLineLen + 1, nLineLen - 1);
                    sLineTmp = "?" + sLineTmp;
                } else
                    sLineTmp = new String(byNew, i * nLineLen, nLineLen);
            }
            sLineTmp = "  " + sLineTmp;
            sRet += "[" + fillChar(Integer.toHexString(i * nLineLen + 1), '0', 4, true) + "-"
                    + fillChar(Integer.toHexString(i * nLineLen + nLineLen), '0', 4, true) + "] ";
            sRet += convertToHex(byNew, i * nLineLen, nLineLen);

            for (int j = 0, n = sRepalce.length(); j < n; j++)
                sLineTmp = sLineTmp.replace(sRepalce.charAt(j), '.');

            sRet += sLineTmp + "\n";
        }

        return (sRet);
    }

    public static String formatInHex(String sSource, int nLineLen) {
        final String sRepalce = "\n\t\r\0";
        byte[] bySrc = sSource.getBytes();
        int nLength = bySrc.length;

        // System.out.println("nLength = "+nLength);
        int nLine = nLength / nLineLen + 1;

        String sLineTmp = "";
        byte[] byNew = new byte[nLine * nLineLen];
        for (int i = 0; i < nLength; i++)
            byNew[i] = bySrc[i];
        // System.out.println("byNew.nLength = "+byNew.length);

        String sRet = "";

        for (int i = 0; i < nLine; i++) {
            /*
             * int k = 0; for( int j=0; j<nLineLen; j++) { if( byNew[i*nLineLen+j]<0 ) k++; }
             */
            if (byNew[(i + 1) * nLineLen - 1] < 0) {
                sLineTmp = new String(byNew, i * nLineLen, nLineLen - 1);
                sLineTmp += "?";
            } else {
                if (byNew[i * nLineLen] < 0) {
                    sLineTmp = new String(byNew, i * nLineLen + 1, nLineLen - 1);
                    sLineTmp = "?" + sLineTmp;
                } else
                    sLineTmp = new String(byNew, i * nLineLen, nLineLen);
            }
            sLineTmp = "  " + sLineTmp;
            sRet += convertToHex(byNew, i * nLineLen, nLineLen);

            for (int j = 0, n = sRepalce.length(); j < n; j++)
                sLineTmp = sLineTmp.replace(sRepalce.charAt(j), '.');

            sRet += sLineTmp + "\n";
        }

        return (sRet);
    }

    /**
     * �ֽ�����ת��Ϊʮ����Ʊ?����ȱʡ������ �������еĽ��
     * 
     * @param byteSrc ���ʱÿһ�еĳ��ȣ�Ҳ����ÿһ�а���ٸ��ֽڵ���Ϣ
     * @param lengthOfLine ÿ����ʾ���ֽ���
     * @return ʮ����Ʊ�
     */
    public static String toHexTable(byte[] byteSrc, int lengthOfLine) {
        return toHexTable(byteSrc, lengthOfLine, 7);
    }

    /**
     * �ֽ�����ת��Ϊʮ����Ʊ?����
     * 
     * @author �뺣��
     * @param byteSrc ��ת�����ֽ�����
     * @param lengthOfLine ���ʱÿһ�еĳ��ȣ�Ҳ����ÿһ�а���ٸ��ֽڵ���Ϣ
     * @param column ����Ϣ �˽�����ݣ�����һλΪ���У��м�һλΪ���У�����һλΪ���С�
     * @return ʮ����Ʊ�
     */
    public static String toHexTable(byte[] byteSrc, int lengthOfLine, int column) {
        // ʮ����Ʊ���ַ����
        StringBuffer hexTableBuffer = new StringBuffer(256);
        // ��Ҫ���������
        // int lineCount =
        // (int)Math.ceil((double)byteSrc.length/(double)lengthOfLine);
        int lineCount = byteSrc.length / lengthOfLine;
        int totalLen = byteSrc.length;
        if (byteSrc.length % lengthOfLine != 0)
            lineCount += 1;
        // ÿ�е��ֽ�����
        byte[] lineByte;
        // ѭ�����ÿһ�е��ַ���Ϣ��
        for (int lineNumber = 0; lineNumber < lineCount; lineNumber++) {
            // ȡ����һ�е�һ���ֽڵ�λ�á�
            int startPos = lineNumber * lengthOfLine;
            // ȡ����һ�е��ֽ����顣
            lineByte = new byte[Math.min(lengthOfLine, totalLen - startPos)];
            System.arraycopy(byteSrc, startPos, lineByte, 0, lineByte.length);

            int columnA = column & 4;
            if (4 == columnA) {
                int count = 10 * lineNumber;
                // ���ַ����������ֽڵ�ַ��Ϣ��
                String addrStr = Integer.toString(count);
                int len = addrStr.length();
                for (int i = 0; i < 8 - len; i++)
                    hexTableBuffer.append('0');
                // addrStr = fillChar(addrStr, '0', 8, true);
                hexTableBuffer.append(addrStr);
                hexTableBuffer.append("h: ");
            }

            int columnB = column & 2;
            if (2 == columnB) {
                // ����һ��дʮ�������
                StringBuffer byteStrBuf = new StringBuffer();
                for (int i = 0; i < lineByte.length; i++) {
                    String num = Integer.toHexString(lineByte[i] & 0xff);
                    if (num.length() < 2)
                        byteStrBuf.append('0');
                    byteStrBuf.append(num);
                    byteStrBuf.append(' ');
                    // byteStrBuf.append(fillChar(Integer.toHexString(lineByte[i]&0xff),
                    // '0', 2, true)+ " ");
                }
                // hexTableBuffer.append(byteStrBuf);
                hexTableBuffer.append(fillChar(byteStrBuf.toString(), ' ', 48, false));
                hexTableBuffer.append("; ");
            }

            int columnC = column & 1;
            if (1 == columnC) {
                // д�����ַ�
                for (int i = 0; i < lineByte.length; i++) {
                    char c = (char) lineByte[i];
                    // ����ַ��ASCII��С��33����ʾ"."��
                    if (c < 33)
                        c = '.';
                    try {
                        // ����������ַ����ʾ�����ַ�
                        if (c >= 0xa0 && i < (lineByte.length - 1)) {
                            char c2 = (char) lineByte[i + 1];
                            if (c2 >= 0xa0) {
                                String str = new String(lineByte, i, 2);
                                hexTableBuffer.append(str);
                                i++;
                                continue;
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    hexTableBuffer.append("");
                    hexTableBuffer.append(c);
                }
            }
            if (lineNumber >= lineCount - 1)
                break;
            hexTableBuffer.append('\n');
        }
        return hexTableBuffer.toString();
    }

    public static int hexStrToInt(String hex) {
        String h = hex.replaceAll(" ", "");
        return Integer.parseInt(h, 16);
    }

    public static String intToHexStr(int i, int len) {
        String h = Integer.toHexString(i);
        boolean needFill = h.length() % 2 != 0;
        int l = needFill ? (h.length() / 2 + 1) : (h.length() / 2);
        if (needFill)
            h = "0" + h;

        StringBuffer buffer = new StringBuffer();
        for (int j = 0; j < (len - l); j++) {
            buffer.append("00");
            buffer.append(" ");
        }

        for (int j = 0; j < l; j++) {
            buffer.append(substr(h, j * 2, 2));
            buffer.append(" ");
        }
        if (buffer.charAt(buffer.length() - 1) == ' ')
            buffer.deleteCharAt(buffer.length() - 1);
        return buffer.toString();
    }

    public static String listToString(List list, char sep) {
        StringBuffer buffer = new StringBuffer();
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                if (buffer.length() > 0) {
                    buffer.append(sep);
                }
                buffer.append(list.get(i));
            }
        }
        return buffer.toString();
    }

    public static String arrayToString(Object[] objs) {
        StringBuffer buffer = new StringBuffer();
        if (objs != null) {
            for (int i = 0; i < objs.length; i++) {
                if (buffer.length() > 0)
                    buffer.append("\n");
                buffer.append(objs[i]);
            }
        }
        return buffer.toString();
    }

    public static String addQuot(String s) {
        if (s.charAt(0) != '\"' && s.charAt(s.length() - 1) != '\"') {
            return '\"' + s + '\"';
        }
        return s;
    }

    public static String toUnicode(String input) {
        StringBuffer ret = new StringBuffer();
        for (int i = 0; i < input.length(); i++) {
            char ch = input.charAt(i);
            if ((!Character.isWhitespace(ch) && ch < 0x20 || ch > 0x7e)) {
                ret.append("\\u");
                // requires 1.5 VM
                // ret.append(String.format("%1$04x", new Object[] {
                // Integer.valueOf(ch) }));
                ret.append(fillChar(Integer.toHexString(ch), '0', 4, true));
            } else {
                ret.append(ch);
            }
        }
        return ret.toString();
    }

    public static String stringToUnicode(String str) {
        int len = str.length();
        StringBuffer buf = new StringBuffer(len);
        for (int i = 0; i < len; i++) {
            char c = str.charAt(i);
            buf.append("\\u");
            buf.append(toHex(c >> 12 & 0xf));
            buf.append(toHex(c >> 8 & 0xf));
            buf.append(toHex(c >> 4 & 0xf));
            buf.append(toHex(c >> 0 & 0xf));
        }

        return buf.toString();
    }

    static char toHex(int nibble) {
        return hexDigit[nibble & 0xf];
    }

    public static String unicodeToString(String escaped) {
        int len = escaped.length();
        StringBuffer buf = new StringBuffer();
        int value = 0;
        for (int x = 0; x < len;) {
            int c = escaped.charAt(x++);
            if (c == 92) {
                c = escaped.charAt(x++);
                if (c == 117) {
                    value = 0;
                    for (int i = 0; i < 4; i++) {
                        c = escaped.charAt(x++);
                        switch (c) {
                        case 48: // '0'
                        case 49: // '1'
                        case 50: // '2'
                        case 51: // '3'
                        case 52: // '4'
                        case 53: // '5'
                        case 54: // '6'
                        case 55: // '7'
                        case 56: // '8'
                        case 57: // '9'
                            value = ((value << 4) + c) - 48;
                            break;

                        case 97: // 'a'
                        case 98: // 'b'
                        case 99: // 'c'
                        case 100: // 'd'
                        case 101: // 'e'
                        case 102: // 'f'
                            value = ((value << 4) + 10 + c) - 97;
                            break;

                        case 65: // 'A'
                        case 66: // 'B'
                        case 67: // 'C'
                        case 68: // 'D'
                        case 69: // 'E'
                        case 70: // 'F'
                            value = ((value << 4) + 10 + c) - 65;
                            break;

                        case 58: // ':'
                        case 59: // ';'
                        case 60: // '<'
                        case 61: // '='
                        case 62: // '>'
                        case 63: // '?'
                        case 64: // '@'
                        case 71: // 'G'
                        case 72: // 'H'
                        case 73: // 'I'
                        case 74: // 'J'
                        case 75: // 'K'
                        case 76: // 'L'
                        case 77: // 'M'
                        case 78: // 'N'
                        case 79: // 'O'
                        case 80: // 'P'
                        case 81: // 'Q'
                        case 82: // 'R'
                        case 83: // 'S'
                        case 84: // 'T'
                        case 85: // 'U'
                        case 86: // 'V'
                        case 87: // 'W'
                        case 88: // 'X'
                        case 89: // 'Y'
                        case 90: // 'Z'
                        case 91: // '['
                        case 92: // '\\'
                        case 93: // ']'
                        case 94: // '^'
                        case 95: // '_'
                        case 96: // '`'
                        default:
                            return "malformed.unicode.encoding";
                        }
                    }

                    buf.append((char) value);
                } else {
                    if (c == 116)
                        c = 9;
                    else if (c == 114)
                        c = 13;
                    else if (c == 110)
                        c = 10;
                    else if (c == 102)
                        c = 12;
                    buf.append(c);
                }
            } else {
                buf.append(c);
            }
        }

        return buf.toString();
    }

}
