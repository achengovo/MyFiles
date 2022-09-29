package com.achengovo.myfiles.utils;

/**
 * 根据文件名判断文件类型
 */
public class JudgeFileType {
    /**
     * 根据文件名判断文件类型
     *
     * @param filename 文件名
     * @return 文件类型
     */
    public static String judgeType(String filename) {
        String[] fileNameSplit = filename.split("\\.");
        if (fileNameSplit.length < 2) {
            return "未知";
        } else {
            String fileNameEnd = fileNameSplit[fileNameSplit.length - 1];
            if (judgeNameEndType(new String[]{"mp3"}, fileNameEnd)) {
                return "music";
            } else if (judgeNameEndType(new String[]{"mp4", "mkv", "webm", "ogg"}, fileNameEnd)) {
                return "video";
            } else if (judgeNameEndType(new String[]{"doc", "docx"}, fileNameEnd)) {
                return "word";
            } else if (judgeNameEndType(new String[]{"ppt", "pptx"}, fileNameEnd)) {
                return "ppt";
            } else if (judgeNameEndType(new String[]{"xlsx", "xls"}, fileNameEnd)) {
                return "excel";
            } else if ("pdf".equalsIgnoreCase(fileNameEnd)) {
                return "pdf";
            } else if (judgeNameEndType(new String[]{"jpg", "jpeg", "png", "ico", "gif"}, fileNameEnd)) {
                return "pic";
            } else if (judgeNameEndType(new String[]{"txt", "xml", "html", "js", "css", "py", "json", "java"}, fileNameEnd)) {
                return "text";
            }
        }
        return null;
    }

    /**
     * 根据后缀及后缀数组判断是否为该类型
     *
     * @param fileNameEnd
     * @param end
     * @return
     */
    public static boolean judgeNameEndType(String[] fileNameEnd, String end) {
        for (int i = 0; i < fileNameEnd.length; i++) {
            if (fileNameEnd[i].equalsIgnoreCase(end)) {
                return true;
            }
        }
        return false;
    }
}
