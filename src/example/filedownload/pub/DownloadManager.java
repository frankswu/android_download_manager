package example.filedownload.pub;

import java.io.File;
import java.net.MalformedURLException;

import android.content.Context;
import example.filedownload.Utils;

/**
 * 单downloadtask的manager
 * 
 *
 */
public class DownloadManager {

	private static DownloadManager manager;
	private static Context mContext;
	private DownloadTask task;
	
	
	
	private DownloadManager(Context context) {
		this.mContext = context;
	}
	
	public static DownloadManager getInstance(Context context) {
		if (manager == null) {
			manager = new DownloadManager(context);
		}
		return manager;
	}
	
	public synchronized void startDownload(String url, String path, String fileName, DownloadTaskListener listener) {
		if (!Utils.isSDCardPresent()) {
//			Toast.makeText(this, "未发现SD卡", Toast.LENGTH_LONG);
			return;
		}

		if (!Utils.isSdCardWrittenable()) {
//			Toast.makeText(this, "SD卡不能读写", Toast.LENGTH_LONG);
			return;
		}
		
		File file = new File(path	+ fileName);
		if (file.exists())
			file.delete();
		try {
			task = new DownloadTask(mContext, url,	path, listener);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		task.execute();
	}

	public synchronized void pauseDownload() {
		if (task != null) {
			task.onCancelled();
		}
	}


	public synchronized void continueDownload(String url, String path, String fileName, DownloadTaskListener listener) {
		task = null;
		try {
			task = new DownloadTask(mContext, url,	path, listener);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			
		}
		task.execute();
	}

	
	
	
	public interface DownloadTaskListener {
		public void pauseProcess(DownloadTask mgr);		//下载进度暂停
	    public void updateProcess(DownloadTask mgr);			// 更新进度
	    public void finishDownload(DownloadTask mgr);			// 完成下载
	    public void preDownload(DownloadTask mgr);					// 准备下载
	    public void errorDownload(DownloadTask mgr, int error);				// 下载错误
	}
	
}
