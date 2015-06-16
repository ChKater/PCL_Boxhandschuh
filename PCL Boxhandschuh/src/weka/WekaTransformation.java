package weka;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WekaTransformation {

	private static final int NUMBER_OF_SAMPLES = 40;

	public static void main(String[] args) {
		File dataDir = new File("data");
		List<String> dataSets = new ArrayList<String>();
		String header = "type,";
		for (int i = 0; i < 40; i++) {
			header += "ACCX_" + i + ",";
			header += "ACCY_" + i + ",";
			header += "ACCZ_" + i + ",";
			header += "GYRX_" + i + ",";
			header += "GYRY_" + i + ",";
			header += "GYRZ_" + i + ",";
		}
		header = header.substring(0, header.length() - 1);
		dataSets.add(header);
		try {

			for (File file : dataDir.listFiles()) {
				if(file.isFile()){
					String prefix = file.getName().split("_")[0];
					prefix = prefix.replace(" ", "");
					BufferedReader br = new BufferedReader(new FileReader(file));
					ArrayList<String> lines = new ArrayList<>();
					String line;
					while ((line = br.readLine()) != null) {
						lines.add(line);
					}
					if (lines.size() > 41) {
						// resample
						int remove = (lines.size() - NUMBER_OF_SAMPLES) / 2;
						for (int i = 0; i < remove; i++) {
							lines.remove(0);
						}
						while (lines.size() > 40) {
							lines.remove(lines.size() - 1);
						}
						String data = prefix + ",";
						for (int i = 0; i < lines.size(); i++) {
							String row = lines.get(0);
							String[] rowData = row.split(",");
							for (int j = 2; j < 8; j++) {
								data += rowData[j] + ",";
							}
						}
						data = data.substring(0, data.length() - 1);
						dataSets.add(data);
					}
					br.close();
				}
			}

			BufferedWriter bw = new BufferedWriter(new FileWriter(new File(
					dataDir, "weka.csv")));
			for (int i = 0; i < dataSets.size(); i++) {
				bw.write(dataSets.get(i) + "\n");
			}
			bw.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
