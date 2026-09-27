package com.example.demo.model;

public class Question {
	int qno;
	String quesName;
boolean ans;
int marks;
boolean userans; 
public int getMarks() {
	return marks;
}
public void setMarks(int marks) {
	this.marks = marks;
}
public boolean isUserans() {
	return userans;
}
public void setUserans(boolean userans) {
	this.userans = userans;
}
public int getQno() {
	return qno;
}
public void setQno(int qno) {
	this.qno = qno;
}
public String getQuesName() {
	return quesName;
}
public void setQuesName(String quesName) {
	this.quesName = quesName;
}
public boolean isAns() {
	return ans;
}
public void setAns(boolean ans) {
	this.ans = ans;
}

}
