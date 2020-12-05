
package com.tk.sz.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import com.tk.sz.model.VoteUserVo;


public class CrlandUtils {


	private static Logger logger = LoggerFactory.getLogger(CrlandUtils.class);
	
	public static ArrayList<String> getFiles(String path) {
		ArrayList<String> files = new ArrayList<String>();
		File file = new File(path);
		File[] tempList = file.listFiles();

		for (int i = 0; i < tempList.length; i++) {
			if (tempList[i].isFile()) {
				System.out.println("文     件：" + tempList[i]);
				files.add(tempList[i].toString());
			}
			if (tempList[i].isDirectory()) {
				System.out.println("文件夹：" + tempList[i]);
			}
		}
		return files;
	}

	private static String getValue(XSSFCell hssfCell) {

		if (null != hssfCell) {

			if (hssfCell.getCellType() == hssfCell.CELL_TYPE_BOOLEAN) {
				// 返回布尔类型的值
				return String.valueOf(hssfCell.getBooleanCellValue()).trim();
			} else if (hssfCell.getCellType() == hssfCell.CELL_TYPE_NUMERIC) {

				hssfCell.setCellType(hssfCell.CELL_TYPE_STRING);
				// 返回数值类型的值
				return hssfCell.getStringCellValue().trim();
			} else {
				// 返回字符串类型的值
				return String.valueOf(hssfCell.getStringCellValue()).trim();
			}
		} else {
			return "";
		}
	}
	
	
	public static ArrayList<VoteUserVo> read(String fileName) {
		File file = new File(fileName);

		if (!file.exists()) {
			System.out.println("文件不存在");
		}

		ArrayList<VoteUserVo> peopoleList = new ArrayList<VoteUserVo>();

		try {
			// 1.读取Excel的对象
			InputStream inputStream = new FileInputStream(fileName);

			XSSFWorkbook xssfWorkbook = new XSSFWorkbook(inputStream);

			XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(0); // 读取第一个sheet

			for (int i = 1; i <= xssfSheet.getLastRowNum(); i++) {

				XSSFRow row = xssfSheet.getRow(i);
				VoteUserVo voteUserVo = null;

				for (int c = 0; c < 19; c++) {

					if (StringUtils.isEmpty(getValue(row.getCell(0)))) {
						break;
					}

					try {
						voteUserVo = new VoteUserVo();

						voteUserVo.setUsercode(getValue(row.getCell(0)).trim());

						if (!StringUtils.isEmpty(getValue(row.getCell(1)))) {
							voteUserVo.setUsername(getValue(row.getCell(1)).trim());
						}
						if (!StringUtils.isEmpty(getValue(row.getCell(2)))) {
							voteUserVo.setUserdeptchannel(getValue(row.getCell(2)).trim());
						}
						if (!StringUtils.isEmpty(getValue(row.getCell(3)))) {
							voteUserVo.setUserdept(getValue(row.getCell(3)).trim());
						}

						if (!StringUtils.isEmpty(getValue(row.getCell(4)))) {
							voteUserVo.setUsertel(getValue(row.getCell(4)).trim());
						}

						String voteTimes = getValue(row.getCell(5));
						if (StringUtils.isEmpty(voteTimes)) {
							voteTimes = "0";
						}
						voteUserVo.setVotetimes(Integer.valueOf(voteTimes));

					} catch (Exception e) {
						e.printStackTrace();
						logger.info("解析excel失败" + voteUserVo.getUsercode() + "  " + voteUserVo.getUsername() + "  "
								+ voteUserVo.getVotetimes());
						continue;
					}
				}

				if (voteUserVo != null) {
					logger.info(voteUserVo.getUsercode() + "  " + voteUserVo.getUsername() + "  "
							+ voteUserVo.getVotetimes());
					peopoleList.add(voteUserVo);
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		return peopoleList;

	}

	/**
	 * 移动文件
	 * 
	 * @param fileName
	 * @param destinationFloderUrl
	 * @return
	 */
	public static boolean removeFile(String fileName, String destinationFloderUrl) {
		File file = new File(fileName);
		File destFloder = new File(destinationFloderUrl);
		// 检查目标路径是否合法
		if (destFloder.exists()) {
			if (destFloder.isFile()) {
				System.out.println("目标路径是个文件，请检查目标路径！");
				return false;
			}
		} else {
			if (!destFloder.mkdirs()) {
				File fileParent = file.getParentFile();
				if (!fileParent.exists()) {
					fileParent.mkdirs();
				}
				System.out.println("目标文件夹不存在，创建目标文件夹");
			}
		}
		// 检查源文件是否合法
		if (file.isFile() && file.exists()) {
			String destinationFile = destinationFloderUrl + "/" + file.getName();
			if (!file.renameTo(new File(destinationFile))) {
				System.out.println("移动文件失败！");
				return false;
			}
		} else {
			System.out.println("要备份的文件路径不正确，移动失败！");
			return false;
		}
		System.out.println("已成功移动文件" + file.getName() + "到" + destinationFloderUrl);
		return true;
	}

}
