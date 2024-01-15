package pillars

import cats.effect.ExitCode
import cats.effect.IO
import cats.effect.IOApp
import com.monovore.decline.Command

trait EntryPoint extends IOApp:

    def app: App[IO]

    override final def run(args: List[String]): IO[ExitCode] =
        Command(app.name, app.description)(CommandOptions.config).parse(args, sys.env) match
        case Left(help)        => IO(System.err.println(help)).as(ExitCode.Error)
        case Right(configPath) =>
            Pillars(configPath).use: pillars =>
                app.run(pillars).as(ExitCode.Success)
end EntryPoint
