using System;
using System.IO;
using System.Text.RegularExpressions;

public class LinesCounter
{
    private string path;
    private Regex lineComment = new Regex("^\\s*//");
    private Regex commentBlockStart = new Regex("^\\s*/\\*");
    private Regex commentBlockEnd = new Regex("\\*/\\s*$");
    private Regex emptyLine = new Regex("^\\s*$");

    public LinesCounter (string path) {this.path = path;}

    // Some comment for the test
    private void count(string root) 
    {
        foreach (string newRoot in Directory.GetDirectories(root))
        {
            count(newRoot);
        }
        
        foreach (string filePath in Directory.GetFiles(root))
        {
            FileInfo fi = new FileInfo(filePath);
            if(fi.Extension.Equals(".cs"))
            {
                int cnt = 0;
                StreamReader reader = fi.OpenText();
                string line;
                bool commentBlock = false;
                while((line = reader.ReadLine()) != null) 
                {
                    if(!commentBlock && !lineComment.IsMatch(line))
                    {
                        if(commentBlockStart.IsMatch(line))
                        {
                            commentBlock = true;
                        } else if (!emptyLine.IsMatch(line)) {
                            cnt++;
                        }
                    } else if (commentBlockEnd.IsMatch(line)) {
                        commentBlock = false;
                    }
                }
                Console.WriteLine("{0} got {1} lines with code", filePath, cnt);
            }
        }
    }

    /* block comment
     * is inserted
     * here
     * just for the
     */

    public void count(){ count(path); }
}

// Fast test class
public class Tester
{
    static void Main(string[] args) 
    {
        LinesCounter lc = new LinesCounter(args[0]);
        lc.count();
    }
}
