# go to root directory
# shellcheck disable=SC2164
cd ~

#mkdir test_directory
if [ -d "test_directory" ]; then
  rm -r test_directory
fi
mkdir test_directory

# creation of folder with folders and files
# shellcheck disable=SC2164
cd test_directory/
touch text.txt
touch text2.txt
touch text3.txt
touch img.png
mkdir folder
mkdir folder/folder1
touch folder/text4.txt
touch folder/text5.txt
mkdir folder/folder1/folder2
touch folder/folder1/folder2/text6.txt

echo "bash script finished!!!"
echo ""
