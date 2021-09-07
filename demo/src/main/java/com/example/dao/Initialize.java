package com.example.dao;

public class Initialize {

		public String msg;
		public Body body;

		public Initialize(String msg, Body body) {
			this.msg = msg;
			this.body = body;
		}

		public String getMsg() {
			return msg;
		}

		public Body getBody() {
			return body;
		}
		
		public void setMsg(String msg) {
			this.msg = msg;
			
		}

		public void setBody(Body body) {
			this.body = body;
		}
	
}


