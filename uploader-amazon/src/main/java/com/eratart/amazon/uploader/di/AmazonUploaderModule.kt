package com.eratart.amazon.uploader.di

import com.amazonaws.auth.AWSCredentials
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.transfer.TransferManager
import com.eratart.amazon.uploader.AmazonConstants
import com.eratart.amazon.uploader.manager.AmazonFileUploaderManager
import com.eratart.uploader.api.IUploaderManager
import com.eratart.uploader.api.UploaderType
import org.koin.core.qualifier.named
import org.koin.dsl.module

val AmazonUploaderModule = module {
    single<AWSCredentials> {
        BasicAWSCredentials(AmazonConstants.ACCESS_KEY_ID, AmazonConstants.SECRET_ACCESS_KEY)
    }
    single { AmazonS3Client(get<AWSCredentials>()) }
    single { TransferManager(get<AmazonS3Client>()) }
    single<IUploaderManager>(named(UploaderType.AMAZON_S3.name)) {
        AmazonFileUploaderManager(get(), get())
    }

}