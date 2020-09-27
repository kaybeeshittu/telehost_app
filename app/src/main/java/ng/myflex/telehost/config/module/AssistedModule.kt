package ng.myflex.telehost.config.module

import com.squareup.inject.assisted.dagger2.AssistedModule
import dagger.Module

@Module(includes = [AssistedInject_AssistedModule::class])
@AssistedModule
interface AssistedModule