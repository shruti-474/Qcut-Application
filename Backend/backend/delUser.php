<?php
require_once("databaseconnect.php");

// Get 'id' from POST request
$id = $_POST['id'] ?? '';

$response = array();

// Validate ID
if(!empty($id)) {

    // Delete query
    $sql = "DELETE FROM userdata WHERE id='$id'";
    $result = mysqli_query($connection, $sql);

    if($result) {
        $response['status'] = 'success';
        $response['message'] = 'User deleted successfully.';
    } else {
        $response['status'] = 'error';
        $response['message'] = 'Failed to delete User.';
    }

} else {
    $response['status'] = 'error';
    $response['message'] = 'ID is missing.';
}

// Output JSON response
header('Content-Type: application/json');
echo json_encode($response);

// Close the database connection
mysqli_close($connection);
?>
