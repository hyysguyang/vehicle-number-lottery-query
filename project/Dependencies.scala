import sbt._

/**
  * @author <a href="mailto:hyysguyang@gmail.com">Young Gu</a>
  * @author <a href="mailto:Young.Gu@lifcosys.com">Young Gu</a>
  */
object Dependencies {
  // Scala related dependency
  val scalatest = "org.scalatest" %% "scalatest" % "2.2.5"

  def scalaDependencies = compile(scalatest)

  def dependencies = scalaDependencies


  def compile(deps: ModuleID*): Seq[ModuleID] = deps map (_ % "compile")
}
