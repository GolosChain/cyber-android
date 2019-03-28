package io.golos.cyber4j.model

import io.golos.cyber4j.services.model.ApiResponseError
import io.golos.cyber4j.utils.Either

enum class PostsFeedType {
    COMMUNITY, SUBSCRIPTIONS, USER_POSTS;

    override fun toString(): String {
        return when (this) {
            COMMUNITY -> "community"
            SUBSCRIPTIONS -> "subscriptions"
            USER_POSTS -> "byUser"
        }
    }
}

enum class CommentsOrigin {
    COMMENTS_OF_USER,
    COMMENTS_OF_POST;

    override fun toString(): String {

        return when (this) {
            COMMENTS_OF_USER -> "user"
            COMMENTS_OF_POST -> "post"
        }
    }
}

enum class DiscussionTimeSort {
    SEQUENTIALLY, INVERTED;

    override fun toString(): String {
        return when (this) {
            SEQUENTIALLY -> "time"
            INVERTED -> "timeDesc"
        }
    }
}
/** listener interface for auth state in cyber microservices.
 *
 * */
interface AuthListener {
    fun onAuthSuccess(forUser: CyberName)
    fun onFail(e: Exception)
}

interface ApiService {
    //type <string>('community')  // Тип ленты
//[
//community             // Лента комьюнити, требует communityId
//| subscriptions         // Лента подписок пользователя, требует userId
//| byUser                // Лента постов самого пользователя, требует userId
//]
//sortBy <string>('time')     // Способ сортировки
//[
//time                  // Сначала старые, потом новые
//| timeInverted          // Сначала новые, потом старые
//]
//sequenceKey <string/null>   // Идентификатор пагинации для получения следующего контента
//limit <number>              // Количество элементов
//userId <string/null>        // Идентификатор пользователя
//communityId <string>        // Идентификатор комьюнити

    fun getDiscussions(feedType: PostsFeedType,
                       sort: DiscussionTimeSort,
                       sequenceKey: String?,
                       limit: Int,
                       userId: String?,
                       communityId: String?): Either<DiscussionsResult, ApiResponseError>

//    content.getPost:                    // Получение конкретного поста
//    params:                         // Параметры запроса из гейта
//    userId <string>             // Идентификатор пользователя
//    permlink <string>           // Пермлинк поста
//    refBlockNum <number>        // Привязанный блок поста

    fun getPost(userId: String,
                permlink: String,
                refBlockNum: Long): Either<CyberDiscussion, ApiResponseError>

    fun getComment(userId: String,
                      permlink: String,
                      refBlockNum: Long): Either<CyberDiscussion, ApiResponseError>

//    content.getComments:                // Получение ленты комментариев
//    params:                         // Параметры запроса из гейта
//    sortBy <string>('time')     // Способ сортировки
//    [
//    time                  // Сначала старые, потом новые
//    | timeInverted          // Сначала новые, потом старые
//    ]
//    sequenceKey <string/null>   // Идентификатор пагинации для получения следующего контента
//    limit <number>(10)          // Количество элементов
//    type <string>('post')       // Тип ленты
//    [
//    user                  // Получить комментарии пользователя, требует userId
//    | post                  // Получить комментарии для поста, требует userId, permlink, refBlockNum
//    ]
//    userId <string/null>        // Идентификатор пользователя
//    permlink <string/null>      // Пермлинк поста
//    refBlockNum <number/null>   // Привязанный блок поста

    fun getComments(sort: DiscussionTimeSort,
                    sequenceKey: String?,
                    limit: Int,
                    origin: CommentsOrigin,
                    userId: String?,
                    permlink: String?,
                    refBlockNum: Long?): Either<DiscussionsResult, ApiResponseError>

//    content.getProfile:                 // Получение профиля пользователя
////    params:                         // Параметры запроса из гейта
////    userId <string>             // Идентификатор пользователя

    fun getUserMetadata(userId: String): Either<UserMetadata, ApiResponseError>

    fun addOnAuthListener(listener: AuthListener)
}

