import com.box.sdk.BoxAPIConnection;
import com.box.sdk.BoxFile;
import com.box.sdk.BoxFolder;
import com.box.sdk.BoxItem.Info;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.Scanner;

public class FilePreview {

	private static final String DEVELOPER_TOKEN = "hQilTmLZhJDXg21E0nz78H0UWolJzXyk";
	private static final String QUERY = "query=";

	@SuppressWarnings("deprecation")
	public static void main(String[] args) {
		System.out.println("Enter folder name to create a folder:");
		Scanner sc = new Scanner(System.in);
		String folderName = sc.next();
		BoxAPIConnection api = new BoxAPIConnection(DEVELOPER_TOKEN);
		BoxFolder rootFolder = BoxFolder.getRootFolder(api);
		rootFolder.createFolder(folderName);
		System.out.println("Enter file location to upload a file:");
		String fileLocation = sc.next();
		System.out.println("Enter file name to save with:");
		String fileName = sc.next();
		Iterator<Info> itemInfo = rootFolder.getChildren().iterator();
		do {
			Info currentItem = itemInfo.next();
			if (currentItem.getName().equalsIgnoreCase(folderName)) {
				FileInputStream stream;
				try {
					BoxFolder folder = new BoxFolder(api, currentItem.getID());
					stream = new FileInputStream(fileLocation);
					BoxFile.Info newFileInfo = folder.uploadFile(stream, fileName);
					stream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;

			}

		} while (itemInfo.hasNext());
		System.out.println("Enter keyword within the file to search for the file:");
		Iterator resultSet = rootFolder.search(QUERY.concat(sc.next())).iterator();
		sc.close();
		while (resultSet.hasNext()) {
			BoxFile.Info info = (BoxFile.Info) resultSet.next();
			BoxFile boxFile = new BoxFile(api, info.getID());
			System.out.println("Preview Link of the file found :" + boxFile.getPreviewLink());
		}

	}

}
