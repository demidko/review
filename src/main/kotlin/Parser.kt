import com.github.javaparser.JavaParser
import com.github.javaparser.ParserConfiguration
import com.github.javaparser.ParserConfiguration.LanguageLevel.JAVA_16_PREVIEW
import com.github.javaparser.ast.ImportDeclaration
import com.github.javaparser.ast.NodeList
import com.github.javaparser.ast.PackageDeclaration
import com.github.javaparser.ast.body.*
import com.github.javaparser.ast.expr.MarkerAnnotationExpr
import com.github.javaparser.ast.expr.NormalAnnotationExpr
import com.github.javaparser.ast.expr.SingleMemberAnnotationExpr
import com.github.javaparser.ast.nodeTypes.NodeWithSimpleName
import com.github.javaparser.ast.stmt.BlockStmt
import com.github.javaparser.printer.DefaultPrettyPrinterVisitor
import com.github.javaparser.printer.configuration.DefaultConfigurationOption
import com.github.javaparser.printer.configuration.DefaultPrinterConfiguration
import com.github.javaparser.printer.configuration.DefaultPrinterConfiguration.ConfigOption.INDENTATION
import com.github.javaparser.printer.configuration.DefaultPrinterConfiguration.ConfigOption.PRINT_COMMENTS
import com.github.javaparser.printer.configuration.Indentation
import com.github.javaparser.printer.configuration.Indentation.IndentType.SPACES
import org.slf4j.LoggerFactory.getLogger
import kotlin.text.RegexOption.MULTILINE

fun String.parseArchitecture(): String {
  val visitor = JavaDeclarationVisitor()
  val result = ParserConfiguration()
    .apply { languageLevel = JAVA_16_PREVIEW }
    .let(::JavaParser)
    .parse(this)

  if (result.problems.isNotEmpty()) {
    val log = getLogger("JavaParser")
    result.problems.forEach {
      log.error(it.toString())
    }
  }

  result.ifSuccessful {
    it.accept(visitor, null)
  }

  return visitor.toString()
}

private class JavaDeclarationVisitor : DefaultPrettyPrinterVisitor(
  DefaultPrinterConfiguration()
    .addOption(DefaultConfigurationOption(PRINT_COMMENTS))
    .addOption(DefaultConfigurationOption(INDENTATION, Indentation(SPACES, 2)))
) {

  private var isInterface = false

  private val blanks = "^(?:[\\t ]*(?:\\r?\\n|\\r))+".toRegex(MULTILINE)

  override fun visit(n: ClassOrInterfaceDeclaration, arg: Void?) {
    isInterface = n.isInterface
    super.visit(OrderedClassOrInterfaceDeclaration(n), arg)
  }

  override fun visit(n: PackageDeclaration, arg: Void?) {}

  override fun visit(n: ImportDeclaration, arg: Void?) {}

  override fun visit(n: MarkerAnnotationExpr, arg: Void?) {}

  override fun visit(n: SingleMemberAnnotationExpr, arg: Void?) {}

  override fun visit(n: NormalAnnotationExpr, arg: Void?) {}

  override fun visit(n: AnnotationDeclaration?, arg: Void?) {}

  override fun visit(n: AnnotationMemberDeclaration?, arg: Void?) {}

  override fun visit(n: InitializerDeclaration?, arg: Void?) {}

  override fun visit(n: ConstructorDeclaration, arg: Void?) {
    if (n.isPublic) super.visit(n, arg)
  }

  override fun visit(n: EnumDeclaration, arg: Void?) {
    if (n.isPublic) super.visit(n, arg)
  }

  override fun visit(n: MethodDeclaration, arg: Void?) {
    if (n.isPublic || isInterface) super.visit(n, arg)
  }

  override fun visit(n: BlockStmt, arg: Void?) {
    printer.print(";")
  }

  override fun visit(n: FieldDeclaration, arg: Void?) {
    if (n.isPublic) super.visit(n, arg)
  }

  override fun toString() = super.toString()
    .replace(blanks, String())
    .replace(" ;", ";")
    .trim()
    .plus('\n')
}

private class OrderedClassOrInterfaceDeclaration(d: ClassOrInterfaceDeclaration) :
  ClassOrInterfaceDeclaration(
    d.tokenRange.orElse(null), d.modifiers, d.annotations, d.isInterface,
    d.name, d.typeParameters, d.extendedTypes, d.implementedTypes, d.members
  ) {
  override fun getMembers() = NodeList(super.getMembers().sortedBy(BodyDeclaration<*>::name))
}

private val BodyDeclaration<*>.name
  get() = when (this) {
    is FieldDeclaration -> variables?.getOrNull(0)?.nameAsString ?: String()
    is NodeWithSimpleName<*> -> nameAsString
    else -> String()
  }
