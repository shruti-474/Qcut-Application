<?php
require_once("databaseconnect.php");



$data = array();

// Fetch shop with specific ID
$sql = "SELECT * FROM userdata";
$result = mysqli_query($connection, $sql);

while ($row = mysqli_fetch_array($result)) {
    array_push($data, array(
        'id' => $row[0],
        'name' => $row[1],
        'mobile' => $row[2],
        'email' => $row[3],
        'adddress' => $row[4],
        'password' => $row[5],
        'image' => $row[6],
        'userrole' => $row[7]
    ));
}

// Return JSON
echo json_encode(array('getUserData' => $data));
?>
