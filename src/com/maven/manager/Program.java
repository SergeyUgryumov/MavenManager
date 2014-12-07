package com.maven.manager;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

import org.apache.maven.shared.invoker.DefaultInvocationRequest;
import org.apache.maven.shared.invoker.DefaultInvoker;
import org.apache.maven.shared.invoker.InvocationRequest;
import org.apache.maven.shared.invoker.Invoker;
import org.apache.maven.shared.invoker.MavenInvocationException;

import com.javafiddle.maven.PomCreator;
import com.javafiddle.web.templates.ClassTemplate;

public class Program {
	
	public static String getProjectPath() {
		return "C:\\apache-maven-3.2.3\\Projects\\MyFirstProject";
	}
	
	public static String getMainPackageName() {//package with Main.java
		return "com.myfirstproject.web";
	}
	
	public static File createFile(String path, String value, String pack) throws IOException {
		String fullPath = path;
		
		if (pack != null) {
			String[] packDirs = pack.split("\\.");
			for (int i = 0; i < packDirs.length; i++) {
				fullPath += packDirs[i] + File.separator;
			}
		}
		fullPath += "AppTest.java";
				
		File file = new File(fullPath);
		file.getParentFile().mkdirs();
		file.createNewFile();
		
		FileWriter wrt = new FileWriter(file);
	
		wrt.append(value);
		//Запись в файл
		wrt.flush();
		wrt.close();
		
		return file;
	}

	public static void main(String[] args) {
		String path = getProjectPath();
		String pack = getMainPackageName();
		
		ClassTemplate ct = new ClassTemplate("AppTest", "JUnit", pack);
		
		try {
			File test = createFile(path + File.separator + "src"
					+ File.separator + "test"
					+ File.separator + "java"
					+ File.separator, ct.getValue(), pack);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		PomCreator pc = new PomCreator("C:\\apache-maven-3.2.3\\Projects\\MyFirstProject");
		File pom = pc.createFile();
		
		InvocationRequest request = new DefaultInvocationRequest();
		request.setPomFile(new File("C:\\apache-maven-3.2.3\\Projects\\MyFirstProject\\pom.xml"));
		//Вместо package можно указать:
		//	compile 
		//	Компилирование проекта 
		//	test
		//	Тестирование с помощью JUnit тестов 
		//	package
		//	Создание .jar файла или war, ear в зависимости от типа проекта 
		//	integration-test
		//	Запуск интеграционных тестов 
		//	install
		//	Копирование .jar (war , ear) в локальный репозиторий 
		//	deploy
		//	публикация файла в удалённый репозиторий
		request.setGoals(Arrays.asList( "clean", "package" ));

		Invoker invoker = new DefaultInvoker();
		invoker.setMavenHome(new File("C:\\apache-maven-3.2.3"));
		try {
			invoker.execute( request );
		} catch (MavenInvocationException e) {
			e.printStackTrace();
		}
		
	}

}
