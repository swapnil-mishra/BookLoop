package com.mypackage.bookloop;

public class BookListModel
{

    private String authorName,bookDescription,bookID,bookName,bookPrice,publisherName,sellerName,sellerPhone,sem,sellerEmail;

    public BookListModel() {
    }

    public BookListModel(String sellerEmail) {
        this.sellerEmail = sellerEmail;
    }

    public BookListModel(String sellerName, String bookName) {
        this.sellerName = sellerName;
        this.bookName = bookName;
    }

    public BookListModel(String authorName, String bookDescription, String bookID, String bookName, String bookPrice, String publisherName, String sellerName, String sellerPhone, String sem,String sellerEmail) {
        this.authorName = authorName;
        this.bookDescription = bookDescription;
        this.bookID = bookID;
        this.bookName = bookName;
        this.bookPrice = bookPrice;
        this.publisherName = publisherName;
        this.sellerName = sellerName;
        this.sellerPhone = sellerPhone;
        this.sem = sem;
        this.sellerEmail=sellerEmail;
    }

    public String getAuthorName() {
        return authorName;
    }

    public String getBookDescription() {
        return bookDescription;
    }

    public String getBookID() { return bookID; }

    public String getBookName() {
        return bookName;
    }

    public String getBookPrice() {
        return bookPrice;
    }

    public String getPublisherName() {
        return publisherName;
    }

    public String getSellerName() {
        return sellerName;
    }

    public String getSellerPhone() {
        return sellerPhone;
    }

    public String getSem() {
        return sem;
    }

    public String getSellerEmail() {
        return sellerEmail;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public void setBookDescription(String bookDescription) {
        this.bookDescription = bookDescription;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public void setBookPrice(String bookPrice) {
        this.bookPrice = bookPrice;
    }

    public void setPublisherName(String publisherName) {
        this.publisherName = publisherName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public void setSellerPhone(String sellerPhone) {
        this.sellerPhone = sellerPhone;
    }

    public void setSem(String sem) {
        this.sem = sem;
    }

    public void setSellerEmail(String sellerEmail) {
        this.sellerEmail = sellerEmail;
    }
}
