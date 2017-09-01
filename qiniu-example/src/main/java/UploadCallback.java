import java.io.IOException;

import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;

public class UploadCallback {
	Zone z = Zone.autoZone();
	Configuration c = new Configuration(z);
	// 创建上传对象
	UploadManager uploadManager = new UploadManager(c);


	//String filePath = "E://test-video//VID_20170320_161534.mp4";
    String filePath = "E://test-video//VID_20170307_110331.mp4";
	//String filePath = "E://test-video//VID_20170302_144739.mp4";
	String storedKey = "25_20170401153516";

	// 设置callbackUrl以及callbackBody,七牛将文件名和文件大小回调给业务服务器
	public String getUpToken() {
		return "Xw5MYn3szGtqZJDTetX3iSfjfvh05n0opzwpuMY2:dnQuLaHC12I7Bd__Cd2ykKYjQAo=:eyJpbnNlcnRPbmx5IjoxLCJkZXRlY3RNaW1lIjoxLCJjYWxsYmFja0JvZHlUeXBlIjoiYXBwbGljYXRpb24vanNvbiIsInNjb3BlIjoiZGV2LXZpZGVvLXByaXZhdGU6MjVfMjAxNzA0MDExNTM1MTYiLCJtaW1lTGltaXQiOiJ2aWRlby8qIiwiZGVhZGxpbmUiOjE0OTEwMzU3MTYsImNhbGxiYWNrQm9keSI6IntcImtleVwiOlwiJChrZXkpXCIsXCJoYXNoXCI6XCIkKGV0YWcpXCIsXCJidWNrZXRcIjpcIiQoYnVja2V0KVwiLFwiZm5hbWVcIjpcIiQoZm5hbWUpXCIsXCJleHRcIjpcIiQoZXh0KVwiLFwiZnNpemVcIjokKGZzaXplKX0ifQ==";
	}

	public void upload() throws IOException {
		try {
			// 调用put方法上传
			Response res = uploadManager.put(filePath, storedKey, getUpToken());
			// 打印返回的信息
			System.out.println(res);
			System.out.println(res.bodyString());
		} catch (QiniuException e) {
			Response r = e.response;
			// 请求失败时打印的异常的信息
			System.out.println(r.toString());
			try {
				// 响应的文本信息
				System.out.println(r.bodyString());
			} catch (QiniuException e1) {
				// ignore
			}
		}
	}

	public static void main(String args[]) throws IOException {
		new UploadCallback().upload();
	}

}