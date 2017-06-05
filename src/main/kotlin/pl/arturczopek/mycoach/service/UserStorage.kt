package pl.arturczopek.mycoach.service

import org.springframework.stereotype.Service
import org.springframework.web.context.annotation.SessionScope
import pl.arturczopek.mycoach.model.database.User

@Service
@SessionScope
open class UserStorage(var currentUser: User?,
                       var currentFbData: FbData?)