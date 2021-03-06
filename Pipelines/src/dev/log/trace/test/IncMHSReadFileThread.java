package dev.log.trace.test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import dev.log.barinel.activity.Matrix;
import dev.log.barinel.mhs.AIM;
import dev.log.trace.Staccato;

public class IncMHSReadFileThread implements Runnable {

	@Override
	public void run() {
		try{
			System.out.println("IncMHSReadFileThread Start input stream");
			String file = "matrix_smaller.txt";
			BufferedReader reader = new BufferedReader(new FileReader(file));
			AIM mhs = new AIM();
			String line;
			ArrayList<String> execution;
			long totalTime = 0;
			int ite = 0;
			while((line = reader.readLine()) != null){
//				System.out.println("interation "+ ite++);
				execution = new ArrayList<String>();
				
				String[] parts = line.split("\t");
				int error = 0;
				if(parts[1].equals("-")) error = 1;
				
				parts = parts[0].split(" ");
				
				for(int i = 0; i < parts.length; i++)
					if(Integer.parseInt(parts[i]) == 1)
						execution.add("c"+(i+1));

//				System.out.println(execution);
				long begin = System.nanoTime();
				mhs.addExecution(execution, error);
				totalTime += System.nanoTime() - begin;
			
//				System.out.println("for matrix " + matrix);
//				Thread.sleep(1000);
			}
			System.out.println(mhs);
			System.out.println("Total time taken: "+(double)totalTime/1000000 + " milis for " + mhs.getHittingSets().size() + " solutions.");
		} catch(IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
