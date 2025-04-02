terraform {
  required_providers { aws = { source = "hashicorp/aws", version = "~> 3.0" } }
}
provider "aws" { region = "us-east-1" }
resource "aws_instance" "server" {
  ami           = "ami-0c55b159cbfafe1f0"  # Amazon Linux 2
  instance_type = "t2.micro"
}