<?php
require_once("databaseconnect.php");
define('UPLOAD_IMAGE_PATH', 'image/');

$response = array();

if (isset($_FILES['pic']['name']) && isset($_POST['tags'])) {
	try {
		$target_file = UPLOAD_IMAGE_PATH . basename($_FILES['pic']['name']);
		
		// Attempt to move the uploaded file to the images directory
		if (move_uploaded_file($_FILES['pic']['tmp_name'], $target_file)) {
			$filename = $_FILES['pic']['name'];
			
			// Use prepared statements to prevent SQL injection
			$stmt = $connection->prepare("UPDATE userdata SET image = ? WHERE email = ?");
			$stmt->bind_param("ss", $filename, $_POST['tags']);
			$stmt->execute();

			if ($stmt->affected_rows > 0) {
				$response['error'] = false;
				$response['message'] = 'File uploaded and database updated successfully';
			} else {
				throw new Exception("Database update failed or no rows affected.");
			}

			$stmt->close();
		} else {
			throw new Exception("File upload to server failed.");
		}
	} catch (Exception $e) {
		$response['error'] = true;
		$response['message'] = $e->getMessage();
	}
} else {
	$response['error'] = true;
	$response['message'] = 'Parameters not provided';
}

// Return JSON response
echo json_encode($response);
?>
