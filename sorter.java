package solutions;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class FilePathSort {
	
		//Utilize the File and Path Classes to sort a file read completely into memory.
		public static void memorySort(String pathToDir, String inputFile, String sortedFile) {
			// Read all lines from a file as a Stream. This method populates lazily as the stream is consumed.
			// A Stream is a sequence of elements supporting sequential and parallel aggregate operations.  
			// Returns a stream consisting of the elements of this stream, sorted according to the provided Comparator. 
			try { 
				Stream<String> lines = Files.lines(Paths.get(pathToDir + inputFile));
			    List<String> sortedLines = lines
			            .sorted(Comparator.comparing(line -> line.substring(0, 10)))
			            .collect(Collectors.toList());
			    System.out.println(sortedLines);
			    Files.write(Paths.get(pathToDir + sortedFile), sortedLines);
			} catch (Exception e) {
			    e.printStackTrace();
			}
			finally {
				lines.close();
			}
		}
		
		
		/*  External sorting is a class of sorting algorithms that can handle massive amounts of data. Required when
			data do not fit into memory of device, must reside in slower memory, ie disk. 2 approaches to external sorting: 
				distribution(like quicksort) or 
				external merge sort. 
			Merge sort uses a hybrid approach where data is 'chunked' into pieces which fit into memory are read, sorted, 
			and written to a temp file. Afterwards, they are merged into a final file. 
			
			TL;DR
				Big Unsorted file -> split into smaller files -> recursively sort/merge smaller files
			
		*/
		public void reduceFile(String sortDirPath, String sortFileName, int sortBuffSize, String tempSortDir) throws IOException{

			// Collect temporary Files into List array for merge phase
			private List<File> tempFileList = new ArrayList<File>();
			
			// File-Access Buffer
			BufferedReader buffr = null;
			BufferedWriter buffw = null;
			
			// Collect lines to List Array, this array is cleared once the max Sorting buffer size is reached
			List<String> readLines = new ArrayList<String>();

			// Get nextline from the file
			String nextLine = null;
			String tempFileName = null;
			// Store current size of buffer
			int currBuffSize = 0;

			try{

				// Create new buffered reader within safety of try/catch	
				buffr = new BufferedReader(new FileReader(pathToDir + inputFile));
				int i = 0;
				while ( (nextLine = buffr.readLine()) != null ){

					readLines.add(nextLine);
					currBuffSize += nextLine.length() + 1;
					
					if ( currBuffSize >= sortBuffSize ){
						
						// If the currBuffSize (number of bytes in memory) has exceeded our sortBufferSize
						// sort the lines based on substring 0 - 1, and write to file
						Collections.sort(readLines, Comparator.comparing(line -> line.substring(0, 10)));
						
						File tf = new File(tempSortDir + sortFileName + "_temp_" + i + "_" + UUID.randomUUID());
						tempFileList.add(tf);
						
						//Write out lines to the temp file, flush stream
						buffw = new BufferedWriter( new OutputStreamWriter( new FileOutputStream(tf)));
						for ( j = 0; j < readLines.size(); j++ ) {
							buffw.write(readLines[j]);
						}
						buffw.flush();
						buffw.close();
						
						
						tempFileList.add(tf);
						readLines.clear();
						currBuffSize = 0;
						i++;

					}

				}

			//write out the remaining chunk
				//TODO make this neater -- handle in the while clause somehow?
			Collections.sort(readLines, Comparator.comparing(line -> line.substring(0, 10)));

			Collections.sort(readLines, Comparator.comparing(line -> line.substring(0, 10)));
			
			File tf = new File(tempSortDir + sortFileName + "_temp_" + i + "_" + UUID.randomUUID());
			tempFileList.add(tf);
			
			//Write out lines to the temp file, flush stream
			buffw = new BufferedWriter( new OutputStreamWriter( new FileOutputStream(tf)));
			for ( j = 0; j < readLines.size(); j++ ) {
				buffw.write(readLines[j]);
			}
			buffw.flush();
			buffw.close();
			
			
			tempFileList.add(tf);
			readLines.clear();
			currBuffSize = 0;
			
			

		}catch(IOException io){

				throw io;

			}finally{

				if ( buffr != null )try{buffr.close();}catch(Exception e){}

			}

		}
		public static String makeTempDir(String tempDir) {
			String tempDirPath = tempDir + UUID.randomUUID(); 
			File td = new File(tempDirPath);
			if (!td.exists()) {
				td.mkdir();
			}
			return tempDirPath;
		}
		public static void externalSort(String sortDirPath, String sortFileName, int maxSortBuffSize, String tempSortDir, String sortedFileName) {
			
			String td = makeTempDir(tempSortDir);
			reduceFile(sortDirPath, sortFileName, maxSortBuffSize, td)
			
			
			
			
					
			
		}
		
		
			
		
		public static void main (String Args[]) {
			sortFile("/home/jstrand", "test.txt", "test-sorted.txt");
			
			
		}
	}


