package com.ftd.smartshare.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.sql.Timestamp;

import com.ftd.smartshare.JDBCConnectionFactory;
import com.ftd.smartshare.entity.Files;


public class FileDao {
	
	// Create - Uploading file
	public boolean createFile(Files file, String password, int expiration, int maxDownloads) {
		if (checkRequirementsForUpload(file.getFileName()) == true) {
			try (Connection connection = getConnection();
		            PreparedStatement stmt = connection.prepareStatement("INSERT INTO smartshare.files" +
		                    "(file_name, file, time_created, expiry_time, max_downloads, total_downloads, password)" +
		                    "VALUES (?, ?, ?, ?, ?, ?, ?)")) {
		            stmt.setString(1, file.getFileName());
		            stmt.setBytes(2, file.getFile());
		            stmt.setTimestamp(3, file.getTimeCreated());
		            stmt.setTimestamp(4, file.getExpiryTime());
		            stmt.setInt(5, file.getMaxDownloads());
		            stmt.setInt(6, file.getTotalDownloads());
		            stmt.setString(7, file.getPassword());
		            stmt.executeUpdate();
		            return true;
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
		}

		System.out.println("File already exists");
	    return false;
	}
	
	// Read - Downloading file
    public byte[] getFileData(String fileName, String password) {  
        try (PreparedStatement stmt = getConnection().prepareStatement("SELECT file FROM smartshare.files" +
                " WHERE smartshare.files.file_name = ?");) {
        	stmt.setString(1, fileName);
            ResultSet resultSet = stmt.executeQuery();
            byte[] result = null;
            
            if (resultSet.next()) {
            	// Make defensive copy
                result = Arrays.copyOf(resultSet.getBytes("file"), resultSet.getBytes("file").length);
            }

            updateTotalDownloads(fileName);
            //System.out.println(Arrays.toString(result));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public boolean checkRequirementsForUpload (String fileName) {
    	try (PreparedStatement stmt = getConnection().prepareStatement("SELECT * FROM smartshare.files" +
                " WHERE smartshare.files.file_name = ?");) {
        	stmt.setString(1, fileName);
            ResultSet resultSet = stmt.executeQuery();
            
            if (resultSet.next()) {
            	if (resultSet.getString("file_name") != fileName) {
            		return false;
            	}
            }

            updateTotalDownloads(fileName);
            //System.out.println(Arrays.toString(result));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }
	
    // Update - Updating number of total downloads
    
    public boolean updateTotalDownloads (String fileName) {
        try (PreparedStatement totalDownloadsStmt = getConnection().prepareStatement("UPDATE smartshare.files SET total_downloads = total_downloads - 1" +
                " WHERE smartshare.files.id = ?");) {
        	totalDownloadsStmt.setString(1, fileName);
        	int numRows = totalDownloadsStmt.executeUpdate();
        	//System.out.println(numRows);
        	return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
	
    // Delete - Delete files
    
    public boolean deleteEntry (String fileName, String password, int maxDownloads) {
    	if (needToDelete(fileName, password, maxDownloads)) {
        	try (PreparedStatement deletion = getConnection().prepareStatement("DELETE FROM smartshare.files" +
                    " WHERE smartshare.files.file_name = ?");) {  
        		deletion.setString(1, fileName);
            	int numRows = deletion.executeUpdate();
            	
            	// For confirmation that a file was affected
            	System.out.println(numRows);
            	
            	return true;
            } catch (SQLException e) {
                e.printStackTrace();
            }
    	}
        return false;

    }
    
    public boolean needToDelete (String fileName, String password, int maxDownloads) {
    	try (PreparedStatement all = getConnection().prepareStatement("SELECT * FROM smartshare.files WHERE" +
    			" smartshare.files.file_name = ?");) {  
    		all.setString(1, fileName);
    		ResultSet allResults = all.executeQuery();

            if (allResults.next()) {
            	if (allResults.getString("file_name") != fileName || allResults.getString("password") != password || 
            			new Timestamp(System.currentTimeMillis()).after(allResults.getTimestamp("expiry_time")) || 
            			allResults.getInt("max_downloads") == maxDownloads){            		
            		return true;
            	}
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

	private Connection getConnection() {
        return JDBCConnectionFactory.getInstance().getConnection();
	}

}
