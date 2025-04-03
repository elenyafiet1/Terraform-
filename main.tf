provider "aws" {
  region     = "us-east-1"  # Change as needed
  access_key = var.AWS_ACCESS_KEY
  secret_key = var.AWS_SECRET_KEY
}

resource "aws_s3_bucket" "example" {
  bucket = "my-example-bucket-12345"
  acl    = "private"
}

variable "AWS_ACCESS_KEY" {}
variable "AWS_SECRET_KEY" {}
