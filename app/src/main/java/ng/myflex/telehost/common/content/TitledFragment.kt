package ng.myflex.telehost.common.content

abstract class TitledFragment : ApplicationFragment() {
    abstract fun getTitle(): Int
}