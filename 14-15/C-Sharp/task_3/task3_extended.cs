using System;
using System.IO;
using System.Text.RegularExpressions;

public class LinesCounter
{
    private string path;
    // A bunch of regular expressions off commentaries
    private Regex lineComment = new Regex("^\\s*//");
    private Regex commentBlockStart = new Regex("^\\s*/\\*");
    private Regex commentBlockEnd = new Regex("\\*/\\s*$");
    private Regex emptyLine = new Regex("^\\s*$");

    public LinesCounter (string path) {
        this.path = path;
    }

    private void count(string root, string ident)
    {
        Console.WriteLine("{0}- {1}", ident, root);
        foreach (string newRoot in Directory.GetDirectories(root))
        {
            count(newRoot, ident + "    ");
        }

        // For each line in given directory
        foreach (string filePath in Directory.GetFiles(root))
        {
            FileInfo fi = new FileInfo(filePath);
            // If file extension `.cs`
            if(fi.Extension.Equals(".cs"))
            {
                // Init counter
                int cnt = 0;
                StreamReader reader = fi.OpenText();
                string line;
                bool commentBlock = false;
                // For each line
                while((line = reader.ReadLine()) != null)
                {
                    // If we not inside a commentary block and current line
                    // not starts with line commentary
                    if(!commentBlock && !lineComment.IsMatch(line))
                    {
                        // If current line is start of commentary block
                        if(commentBlockStart.IsMatch(line))
                        {
                            // Set commentary block marker as true
                            commentBlock = true;
                        // Else if current line is not empty
                        } else if (!emptyLine.IsMatch(line)) {
                            // Increase code lines counter
                            cnt++;
                        }
                    // Else if it is an end of commentary block set commentary
                    // block marker to false. Otherwise go to next iteration.
                    } else if (commentBlockEnd.IsMatch(line)) {
                        commentBlock = false;
                    }
                }
                // Puts collected data to console.
                Console.WriteLine("{0}    File `{1}` contains {2} lines with code",
                                  ident, filePath, cnt);
            }
        }
    }

    /* block comment
     * is inserted
     * here
     * just for the
     * test
     */

    public void count(){ count(path, ""); }
}

/// <summary>
/// This class is entry point. It wants a path to file as first argument
/// </summary>
public class Tester
{
    static void Main(string[] args)
    {
        LinesCounter lc = new LinesCounter(args[0]);
        lc.count();
    }
}
