package com.eratart.domain.interactor.users

import com.eratart.core.constants.StringConstants
import com.eratart.domain.model.domain.Advert
import com.eratart.domain.model.domain.Capsule
import com.eratart.domain.model.domain.Item
import com.eratart.domain.model.domain.Look
import com.eratart.domain.model.domain.Price
import com.eratart.domain.model.domain.Size
import com.eratart.domain.model.enums.Currency
import com.eratart.domain.model.enums.MyAdvertState
import com.eratart.domain.model.enums.Season
import com.eratart.domain.model.enums.SizeType

object MockUtil {


    private fun getLook1(): Look {
        val photo = "https://i.pinimg.com/originals/0f/44/4e/0f444e466e820c50d5fcc395346e1a05.jpg"
        return Look(
            "Look 1",
            "Best look for best person",
            photo
        )
    }

    private fun getLook2(): Look {
        val photo = "https://i.pinimg.com/736x/68/c3/45/68c345fb284dc5a1411998a4e890dd7d.jpg"
        return Look(
            "Look 2",
            "Best look for best person",
            photo
        )
    }

    private fun getLook3(): Look {
        val photo = "https://i.pinimg.com/564x/78/75/38/7875387d95f98f078c314bbf6f6a627b.jpg"
        return Look(
            "Look 3",
            "Best look for best person",
            photo
        )
    }

    fun getLooksList() = listOf(getLook1(), getLook2(), getLook3())

    private fun getItem1(): Item {
        val brand = "House"
        val title = "Pants $brand"
        val category = "Woman ${StringConstants.DOT_BIG} Pants, jeans"
        val photo =
            "https://www.housebrand.com/media/catalog/product/cache/1200/a4e40ebdc3e371adff845072e1c73f37/Z/L/ZL060-99X-050-1.jpg"
        val mark = 1f
        val size = Size("48", SizeType.EUR)
        return Item(
            0L,
            title,
            category,
            listOf(photo),
            mark,
            size,
            brand,
            Season.SUMMER,
            capsules = listOf(),
            looks = getLooksList()
        )
    }

    private fun getItem2(): Item {
        val brand = "Cropp"
        val title = "Hoodie $brand"
        val category = "Woman ${StringConstants.DOT_BIG} Hoodies"
        val photo =
            "https://www.cropp.com/media/catalog/product/cache/1200/a4e40ebdc3e371adff845072e1c73f37/3/2/3298I-99X-050-1_7.jpg"
        val mark = 1f
        val size = Size("43", SizeType.EUR)
        return Item(
            1L,
            title,
            category,
            listOf(photo),
            mark,
            size,
            brand,
            Season.SUMMER,
            capsules = listOf(),
            looks = getLooksList()
        )
    }

    private fun getItem3(): Item {
        val brand = "Nike"
        val title = "Boots $brand"
        val category = "Man ${StringConstants.DOT_BIG} Shoes"
        val photo =
            "https://img01.ztat.net/article/spp-media-p1/bec296b6bc4038adaecfe314d6ab16fe/c1ad99cb2adf44b38510d9fc1c18d34d.jpg?imwidth=762&filter=packshot&imformat=jpeg"
        val mark = 1f
        val size = Size("44", SizeType.EUR)
        return Item(
            2L,
            title,
            category,
            listOf(photo),
            mark,
            size,
            brand,
            Season.SUMMER,
            capsules = listOf(),
            looks = getLooksList()
        )
    }

    private fun getItem4(): Item {
        val brand = "H&M"
        val title = "Shirt $brand"
        val category = "Woman ${StringConstants.DOT_BIG} Shirts"
        val photo =
            "https://cdn.lbtq.io/productImage/resize/300x400_40cd750bba9870f18aada2478b24840a/20211228/103/20211228103130_005947710_1.jpg"
        val mark = 1f
        val size = Size("41", SizeType.EUR)
        return Item(
            3L,
            title,
            category,
            listOf(photo),
            mark,
            size,
            brand,
            Season.SUMMER,
            capsules = listOf(),
            looks = getLooksList()
        )
    }

    private fun getItem5(): Item {
        val brand = "Bane"
        val title = "Jacket $brand"
        val category = "Man ${StringConstants.DOT_BIG} Outfit"
        val photo =
            "https://sc04.alicdn.com/kf/U4633035510cc4b529db12673410effbfo.jpg"
        val mark = 0.4f
        val size = Size("41", SizeType.EUR)
        return Item(
            4L,
            title,
            category,
            listOf(photo),
            mark,
            size,
            brand,
            Season.DEMISEASON,
            capsules = listOf(),
            looks = getLooksList()
        )
    }

    private fun getItem6(): Item {
        val brand = "40 UAH pants"
        val title = "Pants $brand"
        val category = "Man ${StringConstants.DOT_BIG} Pants"
        val photo = "https://i.ytimg.com/vi/ZhubSOkyZBw/maxresdefault.jpg"
        val mark = 0.3f
        val size = Size("41", SizeType.EUR)
        return Item(
            5L,
            title,
            category,
            listOf(photo),
            mark,
            size,
            brand,
            Season.SUMMER,
            capsules = listOf(),
            looks = getLooksList()
        )
    }


    fun getItemsList() =
        listOf(getItem1(), getItem2(), getItem3(), getItem4(), getItem5(), getItem6())

    private fun getCapsule1(): Capsule {
        val title = "Summer capsule"
        val photo =
            "https://modnayakontora.ru/wp-content/uploads/2020/05/E9C6E9CE-6E96-449D-B7A8-E82C141E8621-1024x1024.jpeg"
        val description = "Description 1"
        return Capsule(title, 5, getItemsList().shuffled(), photo, description)
    }

    private fun getCapsule2(): Capsule {
        val title = "Capsule 2"
        val photo =
            "https://modnayakontora.ru/wp-content/uploads/2020/05/219D3A5C-DC20-4A95-ACE2-1B66F6491252-1024x1024.jpeg"
        val description = "Description 2"
        return Capsule(title, 5, getItemsList().subList(0, 4).shuffled(), photo, description)
    }

    fun getCapsulesList() = listOf(getCapsule1(), getCapsule2(), getCapsule2(), getCapsule1())

    private fun getAdvert1(): Advert {
        return Advert(
            0L,
            Price(300.00f, Currency.USD),
            getItem1(),
            null,
            false
        )
    }

    private fun getAdvert2(): Advert {
        return Advert(
            1L,
            Price(300.00f, Currency.USD),
            getItem2(),
            null,
            false
        )
    }

    private fun getAdvert3(): Advert {
        return Advert(
            2L,
            Price(300.00f, Currency.USD),
            getItem3(),
            null,
            false
        )
    }

    private fun getAdvert4(): Advert {
        return Advert(
            3L,
            Price(300.00f, Currency.USD),
            getItem4(),
            null,
            false
        )
    }

    private fun getAdvert5(): Advert {
        return Advert(
            4L,
            Price(300.00f, Currency.USD),
            getItem5(),
            null,
            false
        )
    }

    private fun getAdvert6(): Advert {
        return Advert(
            5L,
            Price(300.00f, Currency.USD),
            getItem6(),
            null,
            false
        )
    }

    fun getAdvertsList(): List<Advert> {
        return listOf(
            getAdvert1(),
            getAdvert2().copy(isFavorite = true),
            getAdvert3().copy(isFavorite = true),
            getAdvert4(),
            getAdvert5(),
            getAdvert6()
        )
    }

    fun getMyAdvertsList(): List<Advert> {
        return listOf(
            getAdvert4().copy(myAdvertState = MyAdvertState.PUBLISHED),
            getAdvert5().copy(myAdvertState = MyAdvertState.MODERATION),
            getAdvert6().copy(myAdvertState = MyAdvertState.REJECTED)
        )
    }

    fun getFavoriteAdvertsList(): List<Advert> {
        return listOf(
            getAdvert2().copy(isFavorite = true),
            getAdvert3().copy(isFavorite = true)
        )
    }

}