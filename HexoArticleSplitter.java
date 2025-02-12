import java.io.*;
import java.util.*;

public class HexoArticleSplitter {

    public static void splitHexoArticle(String inputFile, String outputFile) throws IOException {
        // 读取Hexo的.md文件内容
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        }

        // 按段落分割文章
        String[] paragraphs = content.toString().split("\n\n+");  // 按空行分割段落

        // 每段的总字数和目标分隔长度
        int splitLength = 1500;
        int charCount = 0;
        int sectionNumber = 1;  // 分段编号

        // 用于存储处理后的段落
        List<String> resultParagraphs = new ArrayList<>();

        // 累计字数并进行处理
        for (String paragraph : paragraphs) {
            charCount += paragraph.length();
            resultParagraphs.add(paragraph);

            // 每隔1500字且正好在段落末尾时插入分隔符
            if (charCount >= splitLength) {
                resultParagraphs.add("\n## " + sectionNumber + "\n");
                sectionNumber++;
                charCount = 0;  // 重置字数计数
            }
        }


        // 将处理后的内容写入输出文件
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
            for (String paragraph : resultParagraphs) {
                writer.write(paragraph);
                writer.write("\n");
            }
        }

        System.out.println("处理完成，文件已保存为 " + outputFile);
    }

    // 遍历 source/_posts 目录下的所有 .md 文件
    public static void processAllPosts(String postsDir) throws IOException {
        File dir = new File(postsDir);
        if (dir.exists() && dir.isDirectory()) {
            File[] files = dir.listFiles((d, name) -> name.endsWith(".md"));  // 过滤 .md 文件
            if (files != null) {
                for (File file : files) {
                    String inputFile = file.getAbsolutePath();
                    String outputFile = file.getParent() + "/output/" + file.getName();  // 输出目录

                    // 创建输出目录（如果不存在）
                    File outputDir = new File(file.getParent() + "/output");
                    if (!outputDir.exists()) {
                        outputDir.mkdir();
                    }

                    // 处理每个 .md 文件
                    splitHexoArticle(inputFile, outputFile);
                }
            } else {
                System.out.println("没有找到任何 .md 文件。");
            }
        } else {
            System.out.println("指定的目录不存在或不是一个有效的目录。");
        }
    }

    public static void main(String[] args) {
        // source/_posts 目录路径
        String postsDir = "input";  // Hexo的_posts目录路径

        try {
            // 调用方法遍历并处理所有的.md文件
            processAllPosts(postsDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}