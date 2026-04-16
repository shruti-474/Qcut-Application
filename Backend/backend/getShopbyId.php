<?php
require_once("databaseconnect.php");

// Get 'id' from POST request
$id = $_POST['id'];

$data = array();

// Fetch shop with specific ID
$sql = "SELECT * FROM shopdata WHERE id='$id'";
$result = mysqli_query($connection, $sql);

while ($row = mysqli_fetch_array($result)) {
    array_push($data, array(
        'id' => $row[0],
        'name' => $row[1],
        'address' => $row[2],
        'time' => $row[3],
        'day' => $row[4],
        'service' => $row[5],
        'image' => $row[6],
        'type' => $row[7],
        'lattitude' => $row[8],
        'lobgitude' => $row[9],
        'shopemail'=>$row[10],
        'rateing'=>$row[11],
        'status'=>$row[12],
        'sets'=>$row[13]

    ));
}

// Return JSON
echo json_encode(array('getShop' => $data));
?>
